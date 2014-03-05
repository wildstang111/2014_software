package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class HotGoalDetector extends Subsystem implements IObserver
{
    final int Y_IMAGE_RES = 480;
//    final double VIEW_ANGLE = 49;
    //final double VIEW_ANGLE = 41.7;		//Axis 206 camera
    final double VIEW_ANGLE = 37.4;  //Axis M1011 camera
    final double PI = 3.141592653;

    final int RECTANGULARITY_LIMIT = 30;
    final int ASPECT_RATIO_LIMIT = 22;

    final int TAPE_WIDTH_LIMIT = 37;
    final int VERTICAL_SCORE_LIMIT = 50;
    final int LR_SCORE_LIMIT = 20;

    final int AREA_MINIMUM = 160;

    final int MAX_PARTICLES = 10;
    AxisCamera camera;
    CriteriaCollection cc;

    public class Scores
    {

        double rectangularity;
        double aspectRatioVertical;
        double aspectRatioHorizontal;
    }

    public class TargetReport
    {

        int verticalIndex;
        int horizontalIndex;
        boolean Hot;
        double totalScore;
        double leftScore;
        double rightScore;
        double tapeWidthScore;
        double verticalScore;
    }

    public HotGoalDetector(String name)
    {
        super(name);
        camera = AxisCamera.getInstance("10.1.11.11");
        
        this.registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP);
    }

    public void init()
    {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, AREA_MINIMUM, 65535, false);
    }

    public void update()
    {
    }

    public void notifyConfigChange()
    {
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        if(((BooleanSubject) subjectThatCaused).getValue())
        {
            SmartDashboard.putBoolean("Looking For Hot Goal", true);
            int numberFound = 0;
            for(int i = 0; i < 100; i++)
            {
                if(this.checkForHotGoal()) numberFound++;
            }
            SmartDashboard.putNumber("Hot Goals found in 100 checks", numberFound);
            SmartDashboard.putBoolean("Looking For Hot Goal", false);
        }
    }

    public boolean checkForHotGoal()
    {
        TargetReport target = new TargetReport();
        int verticalTargets[] = new int[MAX_PARTICLES];
        int horizontalTargets[] = new int[MAX_PARTICLES];
        int verticalTargetCount, horizontalTargetCount;
        System.out.println("Hot Goal Detection started");
        try
        {
            ColorImage image = camera.getImage();     // comment if using stored images
//            ColorImage image;                           // next 2 lines read image from flash on cRIO
//            image = new RGBImage("/image.jpg"); 	// get the sample image from the cRIO flash
            BinaryImage thresholdImage = image.thresholdHSV(100, 250, 20, 255, 20, 250);   // keep only green objects

            BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles

            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            horizontalTargetCount = verticalTargetCount = 0;

            if (filteredImage.getNumberParticles() > 0)
            {
                for (int i = 0; i < MAX_PARTICLES && i < filteredImage.getNumberParticles(); i++)
                {
                    ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                    scores[i] = new Scores();

                    scores[i].rectangularity = scoreRectangularity(report);
                    scores[i].aspectRatioVertical = scoreAspectRatio(filteredImage, report, i, true);
                    scores[i].aspectRatioHorizontal = scoreAspectRatio(filteredImage, report, i, false);
                    
                    if(scores[i].aspectRatioHorizontal > scores[i].aspectRatioVertical)
                    {
                        if (scoreCompare(scores[i], false))
                        {
                            System.out.println("particle: " + i + "is a Horizontal Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
                        }
                        else if (scoreCompare(scores[i], true))
                        {
                            System.out.println("particle: " + i + "is a Vertical Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
                        }
                        else
                        {
                            System.out.println("particle: " + i + "is not a Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                        }
                    }
                    else
                    {
                        if (scoreCompare(scores[i], true))
                        {
                            System.out.println("particle: " + i + "is a Vertical Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
                        }
                        else if (scoreCompare(scores[i], false))
                        {
                            System.out.println("particle: " + i + "is a Horizontal Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
                        }
                        else
                        {
                            System.out.println("particle: " + i + "is not a Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                        }
                    }
                    System.out.println("rect: " + scores[i].rectangularity + "ARHoriz: " + scores[i].aspectRatioHorizontal);
                    System.out.println("ARVert: " + scores[i].aspectRatioVertical);
                }

                target.totalScore = target.leftScore = target.rightScore = target.tapeWidthScore = target.verticalScore = 0;
                target.verticalIndex = verticalTargets[0];
                for (int i = 0; i < verticalTargetCount; i++)
                {
                    ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[i]);
                    for (int j = 0; j < horizontalTargetCount; j++)
                    {
                        ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[j]);
                        double horizWidth, horizHeight, vertWidth, leftScore, rightScore, tapeWidthScore, verticalScore, total;

                        horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
                        vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[i], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
                        horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);

                        leftScore = ratioToScore(1.2 * (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / horizWidth);

                        rightScore = ratioToScore(1.2 * (horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth) / horizWidth);

                        tapeWidthScore = ratioToScore(vertWidth / horizHeight);

                        verticalScore = ratioToScore(1 - (verticalReport.boundingRectTop - horizontalReport.center_mass_y) / (4 * horizHeight));
                        total = leftScore > rightScore ? leftScore : rightScore;
                        total += tapeWidthScore + verticalScore;

                        if (total > target.totalScore)
                        {
                            target.horizontalIndex = horizontalTargets[j];
                            target.verticalIndex = verticalTargets[i];
                            target.totalScore = total;
                            target.leftScore = leftScore;
                            target.rightScore = rightScore;
                            target.tapeWidthScore = tapeWidthScore;
                            target.verticalScore = verticalScore;
                        }
                    }
                    target.Hot = hotOrNot(target);
                }

                if (verticalTargetCount > 0)
                {
                    ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(target.verticalIndex);
                    double distance = computeDistance(filteredImage, distanceReport, target.verticalIndex);
                    if (target.Hot)
                    {
                        System.out.println("Hot target located");
                        System.out.println("Distance: " + distance);
                    }
                    else
                    {
                        System.out.println("No hot target present");
                        System.out.println("Distance: " + distance);
                    }
                }
            }

            filteredImage.free();
            thresholdImage.free();
            image.free();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        
        return target.Hot;
    }
    
    double computeDistance(BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException
    {
        double rectLong, height;
        int targetHeight;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);

        height = Math.min(report.boundingRectHeight, rectLong);
        targetHeight = 32;

        return Y_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE * Math.PI / (180 * 2)));
    }
    
    public double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean vertical) throws NIVisionException
    {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = vertical ? (4.0 / 32) : (23.5 / 4);	//Vertical reflector 4" wide x 32" tall, horizontal 23.5" wide x 4" tall

        if (report.boundingRectWidth > report.boundingRectHeight)
        {
            aspectRatio = ratioToScore((rectLong / rectShort) / idealAspectRatio);
        }
        else
        {
            aspectRatio = ratioToScore((rectShort / rectLong) / idealAspectRatio);
        }
        return aspectRatio;
    }
    
    protected boolean scoreCompare(Scores scores, boolean vertical)
    {
        boolean isTarget = true;

        isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
        if (vertical)
        {
            isTarget &= scores.aspectRatioVertical > ASPECT_RATIO_LIMIT;
        }
        else
        {
            isTarget &= scores.aspectRatioHorizontal > ASPECT_RATIO_LIMIT;
        }

        return isTarget;
    }
    
    protected double scoreRectangularity(ParticleAnalysisReport report)
    {
        if (report.boundingRectWidth * report.boundingRectHeight != 0)
        {
            return 100 * report.particleArea / (report.boundingRectWidth * report.boundingRectHeight);
        }
        else
        {
            return 0;
        }
    }
    
    protected double ratioToScore(double ratio)
    {
        return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
    }
    
    protected boolean hotOrNot(TargetReport target)
    {
        boolean isHot = true;

        isHot &= target.tapeWidthScore >= TAPE_WIDTH_LIMIT;
        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);

        return isHot;
    }
}

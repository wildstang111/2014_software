package com.wildstangs.subsystems;

import com.sun.cldc.jna.Pointer;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.inputs.camera.WsCamera;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.logger.FileLogger;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.Relay;
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
    final int Y_IMAGE_RES = 240;
    final int X_IMAGE_RES = 320;

//    final double VIEW_ANGLE = 49;
    //final double VIEW_ANGLE = 41.7;		//Axis 206 camera
    final double VIEW_ANGLE = 37.4;  //Axis M1011 camera Vertical
    final double HORIZ_VIEW_ANGLE = 47.0;  //Axis M1011 camera Horizontal

    final double PI = 3.141592653;

    final int RECTANGULARITY_LIMIT = 30;
    final int ASPECT_RATIO_LIMIT = 22;

    final int TAPE_WIDTH_LIMIT = 37;
    final int VERTICAL_SCORE_LIMIT = 50;
    final int LR_SCORE_LIMIT = 20;

    final int AREA_MINIMUM = 160;

    final int MAX_PARTICLES = 10;
    WsCamera camera;
    CriteriaCollection cc;
    
    protected Relay.Value ledState = Relay.Value.kOff;
    
    protected TargetReport lastReport = null;
    
    public static class HotGoalSideEnum
    {
        protected String denotion;
        protected HotGoalSideEnum(String denotion)
        {
            this.denotion = denotion;
        }

        public String toString()
        {
            return denotion;
        }
        
        public static final HotGoalSideEnum LEFT = new HotGoalSideEnum("LEFT");
        public static final HotGoalSideEnum RIGHT = new HotGoalSideEnum("RIGHT");
        public static final HotGoalSideEnum NONE = new HotGoalSideEnum("NONE");
        public static final HotGoalSideEnum EITHER = new HotGoalSideEnum("EITHER"); //This is mostly for Auto
    }
    
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
        HotGoalSideEnum side;
    }

    public HotGoalDetector(String name)
    {
        super(name);
        
        imageWriteLevel = imageLevel_config.getValue();
        
        this.initCamera();
        
        this.registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_RIGHT);
        this.registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN);
    }
    
    public void initCamera()
    {
        this.killCamera();
        camera = WsCamera.getInstance("10.1.11.11");
    }
    
    public void killCamera()
    {
        WsCamera.killCamera();
        this.camera = null;
    }
    
    public void init()
    {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, AREA_MINIMUM, 65535, false);
        
        ledState = Relay.Value.kOff;
    }

    public void update()
    {
        SmartDashboard.putString("Camera LEDs", "" + ledState);
        OutputManager.getInstance().getOutput(OutputManager.CAMERA_LED_SPIKE_INDEX).set(ledState);
    }
    int cyclesToCheck = 0;
    public void disabledUpdate()
    {
//        cyclesToCheck++;
//        if(cyclesToCheck > 50)
//        {
//            cyclesToCheck = 0;
//            try
//            {
//                camera.getImage();
//            }
//            catch (Throwable t)
//            {
//                t.printStackTrace();
//            }
//        }
    }
    
    public void notifyConfigChange()
    {
        imageWriteLevel = imageLevel_config.getValue();
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        if(((BooleanSubject) subjectThatCaused).getValue())
        {
            if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN)
            {
                SmartDashboard.putBoolean("Looking For Hot Goal", true);
                SmartDashboard.putBoolean("Found Hot Goal", this.checkForHotGoal());
                SmartDashboard.putBoolean("Looking For Hot Goal", false);
            }
            if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_RIGHT)
            {
                ledState = (ledState == Relay.Value.kOff ? Relay.Value.kOn : Relay.Value.kOff);
            }
        }
    }
    
    protected IntegerConfigFileParameter imageLevel_config = new IntegerConfigFileParameter(this.getClass().getName(), "ImageWriteLevel", 0);
    protected int imageWriteLevel;
    
    public boolean checkForHotGoal()
    {
//        boolean killCameraAfter = false;
        if(this.camera == null)
        {
            this.initCamera();
//            killCameraAfter = true;
        }

        TargetReport target = new TargetReport();
        int verticalTargets[] = new int[MAX_PARTICLES];
        int horizontalTargets[] = new int[MAX_PARTICLES];
        int verticalTargetCount, horizontalTargetCount;
        System.out.println("Hot Goal Detection started");
        try
        {
            ColorImage image = camera.getImage();     // comment if using stored images
            image = camera.getImage();
            if(imageWriteLevel >= 1 && imageWriteLevel <= 3)
            {
//                image.write("colorImage.png");
                NIVision.writeFile(image.image, "/ColorImage.jpg");
                System.out.println("Saving Color Image");
            }
//            ColorImage image;                           // next 2 lines read image from flash on cRIO
//            image = new RGBImage("/video.jpg"); 	// get the sample image from the cRIO flash
            BinaryImage thresholdImage = image.thresholdHSV(100, 150, 50, 255, 90, 255);   // keep only green objects
            
            if(imageWriteLevel >= 2 && imageWriteLevel <= 3)
            {
//                thresholdImage.write("thresholdImage.png");
                NIVision.writeFile(thresholdImage.image, "/ThresholdImage.jpg");
                System.out.println("Saving Threshold Image");
            }
            
            BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles
            
            if(imageWriteLevel == 3)
            {
//                filteredImage.write("filteredImage.png");
                NIVision.writeFile(filteredImage.image, "/FilteredImage.jpg");
                System.out.println("Saving Filtered Image");
            }
            
            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            horizontalTargetCount = verticalTargetCount = 0;
            SmartDashboard.putString("Hot Target Side", "None found" );
            FileLogger.getFileLogger().startLogger();
            if (filteredImage.getNumberParticles() > 0)
            {
                for (int i = 0; i < MAX_PARTICLES && i < filteredImage.getNumberParticles(); i++)
                {
                    String particleString = "";
                    ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                    scores[i] = new Scores();

                    scores[i].rectangularity = scoreRectangularity(report);
                    scores[i].aspectRatioVertical = scoreAspectRatioOnRotatedImage(filteredImage, report, i, true);
                    scores[i].aspectRatioHorizontal = scoreAspectRatioOnRotatedImage(filteredImage, report, i, false);
                    particleString += "Particle " + i + " " + report.boundingRectWidth + " by " + report.boundingRectHeight;
                    if(scores[i].aspectRatioHorizontal > scores[i].aspectRatioVertical)
                    {
                        if (scoreCompare(scores[i], false))
                        {
                            particleString += " is a Horizontal Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
                        }
                        else if (scoreCompare(scores[i], true))
                        {
                            particleString += " is a Vertical Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
                        }
                        else
                        {
                            particleString += " is not a Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                        }
                    }
                    else
                    {
                        if (scoreCompare(scores[i], true))
                        {
                            particleString += " is a Vertical Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
                        }
                        else if (scoreCompare(scores[i], false))
                        {
                            particleString += " is a Horizontal Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
                        }
                        else
                        {
                            particleString += " is not a Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y;
                        }
                    }
                    particleString += " rect: " + scores[i].rectangularity + " ARHoriz: " + scores[i].aspectRatioHorizontal;
                    particleString += " ARVert: " + scores[i].aspectRatioVertical;
                    
                    FileLogger.getFileLogger().logData(particleString);
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
                        FileLogger.getFileLogger().logData("Particle Analysis " + i + " Horiz width: " + horizWidth + " Horiz height: " + horizHeight + " Vert Width: "+ vertWidth );
//                        leftScore = ratioToScore(1.2 * (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / horizWidth);
                        //Rotated image uses y axis values for the reports
                        rightScore = ratioToScore(1.2 * (verticalReport.boundingRectTop - verticalReport.boundingRectHeight - horizontalReport.center_mass_y) / horizWidth);
//                        rightScore = ratioToScore(1.2 * (horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth) / horizWidth);
                        //Rotated image uses x axis values
                        leftScore = ratioToScore(1.2 * (horizontalReport.center_mass_y - verticalReport.boundingRectTop ) / horizWidth);

                        tapeWidthScore = ratioToScore(vertWidth / horizHeight);
                        verticalScore = ratioToScore(1 - (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / (4 * horizHeight));
                        
                        //verticalScore = ratioToScore(1 - (verticalReport.boundingRectTop - horizontalReport.center_mass_y) / (4 * horizHeight));
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
                
                //Show this unless one is found below
                target.side = HotGoalSideEnum.NONE;
                if (verticalTargetCount > 0)
                {
                    ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(target.verticalIndex);
                    double distance = computeDistanceOnRotatedImage(filteredImage, distanceReport, target.verticalIndex);
                    if (target.Hot)
                    {
                        if(target.leftScore > LR_SCORE_LIMIT)
                        {
                            target.side = HotGoalSideEnum.LEFT;
                        }
                        else
                        {
                            target.side = HotGoalSideEnum.RIGHT;
                        }
                        FileLogger.getFileLogger().logData("Hot Target Found - Distance: " + distance);
                    }
                    else
                    {
                        FileLogger.getFileLogger().logData("No Hot Target Found - Distance: " + distance);
                    }
                }
                else
                {
                    FileLogger.getFileLogger().logData("Not Hot Target Found");
                }
                SmartDashboard.putString("Hot Target Side", target.side.toString());
            }
            
//            FileLogger.getFileLogger().flushLogger();
            FileLogger.getFileLogger().killLogger();

            filteredImage.free();
            thresholdImage.free();
            image.free();
        }
        catch(Throwable t)
        {
            SmartDashboard.putString("Something Went Wrong in Hot Goal Detection", t.getMessage());
            t.printStackTrace();
        }
        this.lastReport = target;
        
        SmartDashboard.putBoolean("Found Hot Goal", target.Hot);
        
//        if(killCameraAfter)
//        {
//            this.killCamera();
//        }
        
        return target.Hot;
    }
    
    public boolean wasLastTargetHot()
    {
        return lastReport == null ? false : lastReport.Hot;
    }
    
    public HotGoalSideEnum getLastReportSide()
    {
        return lastReport == null ? HotGoalSideEnum.NONE : lastReport.side;
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
    double computeDistanceOnRotatedImage(BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException
    {
        double rectLong, height;
        int targetHeight;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);

        height = Math.min(report.boundingRectWidth, rectLong);
        targetHeight = 32;

        return X_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(HORIZ_VIEW_ANGLE * Math.PI / (180 * 2)));
    }
    
    public double scoreAspectRatioOnRotatedImage(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean vertical) throws NIVisionException
    {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = vertical ? (32.0 / 4) : (4/23.5);	//Vertical reflector 4" wide x 32" tall, horizontal 23.5" wide x 4" tall

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
    
    public void setLedState(boolean on)
    {
        this.ledState = (on ? Relay.Value.kOn : Relay.Value.kOff);
    }
    
    protected boolean hotOrNot(TargetReport target)
    {
        boolean isHot = true;
        
        System.out.println("tw: " + target.tapeWidthScore + " vs: " + target.verticalScore + " ls: " + target.leftScore + " rs: " + target.rightScore);
        
        isHot &= target.tapeWidthScore >= TAPE_WIDTH_LIMIT;
        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);

        return isHot;
    }
}

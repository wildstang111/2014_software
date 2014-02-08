<#assign licenseFirst = "/*">
<#assign licensePrefix = " * ">
<#assign licenseLast = " */">
<#include "../Licenses/license-${project.license}.txt">

<#if package?? && package != "">
package ${package};

</#if>
import com.wildstangs.autonomous.steps.AutonomousStepGroup;
/**
 *
 * @author ${user}
 */
public class ${name} extends AutonomousStepGroup
{
    
    public ${name}()
    {
        
    }
    public void defineSteps()
    {
        addStep(new AutonomousStep);
    }
    public String toString()
    {
        return 
    }
}

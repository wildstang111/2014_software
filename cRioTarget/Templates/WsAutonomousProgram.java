<#assign licenseFirst = "/*">
<#assign licensePrefix = " * ">
<#assign licenseLast = " */">
<#include "../Licenses/license-${project.license}.txt">

<#if package?? && package != "">
package ${package};

</#if>
import com.wildstangs.autonomous.AutonomousProgram;

/**
 *
 * @author ${user}
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class ${name} extends AutonomousProgram
{
    public ${name}()
    {
        super();
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


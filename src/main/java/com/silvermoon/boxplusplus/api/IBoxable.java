package com.silvermoon.boxplusplus.api;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.RecipeMap;

/**
 * Implements this on MTEMultiBlockBase to allow your machine encapsulating in Box System.
 * If you don't have a standard getRecipeMap(), override getRealRecipeMap().
 * Do remember to call boxRegister.registerMachineToBox() on postInit!
 * <p>
 * 为MTEMultiBlockBase实现此接口,将允许Box封装你的机器。如果你的机器有多种模式，重写getRealRecipeMap()来返回你期望封装的那个。
 * 记得在postInit调用一次boxRegister.registerMachineToBox()！
 */
public interface IBoxable {

    /**
     * Set which module your machine will be.
     * <p>
     * 设定模块ID。模块ID可以从模块核心的meta值获取，范围：0-11
     *
     * @return Module ID, refers to the meta of the module core block. Range: 0-11
     */

    int getModuleID();

    /**
     * Should use update module
     * <p>
     * 是否使用升级模块
     *
     * @return true - updated
     */
    boolean isUpdateModule();

    /**
     * Override this if your machine has different mode.
     *
     * @param machine normally "this"
     * @return GT_Recipe_Map - the real recipeMap
     */
    default RecipeMap<?> getRealRecipeMap(MTEMultiBlockBase machine) {
        return machine.getRecipeMap();
    }

    default int getModuleIDSafely() {
        int moduleId = getModuleID();
        if (moduleId < 0 || moduleId > 11) {
            throw new IllegalArgumentException("Module ID must be between 0 and 11.");
        }
        return moduleId;
    }
}

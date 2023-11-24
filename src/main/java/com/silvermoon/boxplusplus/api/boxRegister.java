package com.silvermoon.boxplusplus.api;

import java.util.ArrayList;
import java.util.List;

import com.silvermoon.boxplusplus.boxplusplus;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public class boxRegister {

    public static List<GT_MetaTileEntity_MultiBlockBase> machineList = new ArrayList<>();

    // Use this to add your boxable machine. **Should be called on postInit().**
    public static void registerMachine(GT_MetaTileEntity_MultiBlockBase machine) {
        if (machine instanceof IBoxable) {
            machineList.add(machine);
        } else boxplusplus.LOG.debug("Trying to add a unboxable MultiBlock to Box!");
    }
}

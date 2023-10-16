package com.silvermoon.boxplusplus.common;

import com.silvermoon.boxplusplus.util.BoxRoutings;

public class BoxModule {
    public static BoxModule Atomic_Manipulation_Claw = new BoxModule("Atomic_Manipulation_Claw", 11, 4, -2);
    public static BoxModule Facility_3826 = new BoxModule("Facility_3826", 1, 4, -12);
    public static BoxModule Sinopec_Group = new BoxModule("Sinopec_Group", -9, 4, -2);
    public static BoxModule Residential_Gas_Water_Heater = new BoxModule("Residential_Gas_Water_Heater", 1, 4, 8);
    public static BoxModule AMD_Wafer_Fabrication_Plant = new BoxModule("AMD_Wafer_Fabrication_Plant", 17, 6, -2);
    public static BoxModule Liquid_Level_Regulator = new BoxModule("Liquid_Level_Regulator", 1, 6, -18);
    public static BoxModule Solid_State_Reshaper = new BoxModule("Solid_State_Reshaper", -15, 6, -2);
    public static BoxModule Residential_Flush_Toilet_with_Built_in_Pump = new BoxModule("Residential_Flush_Toilet_with_Built_in_Pump", 1, 6, 14);
    public static BoxModule Extreme_Temperature_Difference_Generation_Tower = new BoxModule("Extreme_Temperature_Difference_Generation_Tower", 23, 6, -1);
    public static BoxModule Superstructure_Assembly_Plant = new BoxModule("Superstructure_Assembly_Plant", 2, 6, -23);
    public static BoxModule Phase_Parallel_Matrix = new BoxModule("Phase_Parallel_Matrix", -21, 6, -1);
    public static BoxModule Arrayed_and_Maneuverable_Hyperbeam_Receiver_Redirector = new BoxModule("Arrayed_and_Maneuverable_Hyperbeam_Receiver_Redirector", 2, 7, -1);
    public static BoxModule CAERULA_ARBOR = new BoxModule("CAERULA_ARBOR", 2, -4, -1);

    public String[][] moduleStructure;
    public int horizontalOffset;
    public int verticalOffset;
    public int depthOffset;
    public final String name;

    public BoxModule(String name, int horizontalOffset, int verticalOffset, int depthOffset) {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.depthOffset = depthOffset;
        this.name = name;
    }

    static {
        Atomic_Manipulation_Claw.moduleStructure = new String[][]{
            {"   ", " C ", "   "},
            {" C ", " Z ", " C "},
            {" C ", " C ", " C "}};
        Facility_3826.moduleStructure = new String[][]{
            {"   ", " C ", "   "},
            {"   ", "CYC", "   "},
            {"   ", "CCC", "   "}};
        Sinopec_Group.moduleStructure = new String[][]{
            {"   ", " C ", "   "},
            {" C ", " X ", " C "},
            {" C ", " C ", " C "}};
        Residential_Gas_Water_Heater.moduleStructure = new String[][]{
            {"   ", " C ", "   "},
            {"   ", "CWC", "   "},
            {"   ", "CCC", "   "}};
        AMD_Wafer_Fabrication_Plant.moduleStructure = new String[][]{
            {"   ", " E ", "   "},
            {" E ", " E ", " E "},
            {" E ", " V ", " E "},
            {" E ", "EEE", " E "}};
        Liquid_Level_Regulator.moduleStructure = new String[][]{
            {"   ", " E ", "   "},
            {"   ", "EEE", "   "},
            {"   ", "EUE", "   "},
            {"   ", "EEE", "   "}};
        Solid_State_Reshaper.moduleStructure = new String[][]{
            {"   ", " E ", "   "},
            {" E ", " E ", " E "},
            {" E ", " T ", " E "},
            {" E ", " E ", " E "}};
        Residential_Flush_Toilet_with_Built_in_Pump.moduleStructure = new String[][]{
            {"   ", " E ", "   "},
            {"   ", "EEE", "   "},
            {"   ", "ESE", "   "},
            {"   ", "EEE", "   "}};
        Extreme_Temperature_Difference_Generation_Tower.moduleStructure = new String[][]{
            {"   ", "   ", " F ", "   ", "   "},
            {"   ", " F ", " R ", " F ", "   "},
            {" F ", " F ", " F ", " F ", " F "},
            {" F ", " F ", " F ", " F ", " F "}};
        Superstructure_Assembly_Plant.moduleStructure = new String[][]{
            {"   ", "   ", "  F  ", "   ", "   "},
            {"   ", "   ", " FQF ", "   ", "   "},
            {"   ", "   ", "FFFFF", "   ", "   "},
            {"   ", "   ", "FFFFF", "   ", "   "}};
        Phase_Parallel_Matrix.moduleStructure = new String[][]{
            {"   ", "   ", " F ", "   ", "   "},
            {"   ", " F ", " P ", " F ", "   "},
            {" F ", " F ", " F ", " F ", " F "},
            {" F ", " F ", " F ", " F ", " F "}};
        Arrayed_and_Maneuverable_Hyperbeam_Receiver_Redirector.moduleStructure = new String[][]{
            {"     ", "     ", "  G  ", "     ", "     "},
            {"     ", "     ", "  G  ", "     ", "     "},
            {"     ", "  G  ", " GNG ", "  G  ", "     "},
            {"  G  ", " GGG ", "GGGGG", " GGG ", "  G  "}};
        CAERULA_ARBOR.moduleStructure = new String[][]{
            {"  G  ", " GGG ", "GGGGG", " GGG ", "  G  "},
            {"     ", "  G  ", " GMG ", "  G  ", "     "},
            {"     ", "     ", "  G  ", "     ", "     "},
            {"     ", "     ", "  G  ", "     ", "     "}};
    }

    public static BoxModule getModuleByIndex(int index) {
        return switch (index) {
            case 0 -> Atomic_Manipulation_Claw;
            case 1 -> Facility_3826;
            case 2 -> Sinopec_Group;
            case 3 -> Residential_Gas_Water_Heater;
            case 4 -> AMD_Wafer_Fabrication_Plant;
            case 5 -> Liquid_Level_Regulator;
            case 6 -> Solid_State_Reshaper;
            case 7 -> Residential_Flush_Toilet_with_Built_in_Pump;
            case 8 -> Extreme_Temperature_Difference_Generation_Tower;
            case 9 -> Superstructure_Assembly_Plant;
            case 10, 11 -> Phase_Parallel_Matrix;
            case 12 -> Arrayed_and_Maneuverable_Hyperbeam_Receiver_Redirector;
            case 13 -> CAERULA_ARBOR;
            default -> null;
        };
    }

    public static int[] transMachinesToModule(BoxRoutings routing) {
        if (routing.special != 0) {
            if (routing.special > 5402) {
                return new int[]{8, 1};
            }
            return new int[]{8, 0};
        }
        return switch (routing.RoutingMachine.getUnlocalizedName().substring(17)) {
            case "industrialmixer.controller.tier.single", "multimachine.chemicalreactor" -> new int[]{0, 0};
            case "gtplusplus.autocrafter.multi", "mxrandomlargemolecularassembler" -> new int[]{1, 0};
            case "multimachine.cracker", "megadistillationtower", "multimachine.adv.distillationtower", "megaoilcracker", "multimachine.distillationtower" ->
                new int[]{2, 0};
            case "multimachine.multifurnace", "industrialthermalcentrifuge.controller.tier.single",
                "industrialarcfurnace.controller.tier.single", "industrialalloysmelter.controller.tier.single" ->
                new int[]{3, 0};
            case "industrialmultimachine.controller.tier.single" -> new int[]{4, 0};
            case "industrialrockcrusher.controller.tier.single", "industrialfluidheater.controller.tier.single" ->
                new int[]{5, 0};
            case "industrialcuttingmachine.controller.tier.01", "industrialmacerator.controller.tier.single",
                "industrialbender.controller.tier.single", "industrialextruder.controller.tier.single",
                "industrialwiremill.controller.tier.single", "industrialhammer.controller.tier.single" ->
                new int[]{6, 0};
            case "industrialwashplant.controller.tier.single", "industrialsifter.controller.tier.single",
                "industrialcentrifuge.controller.tier.single", "industrialelectrolyzer.controller.tier.single",
                "digester", "basicmachine.electromagneticseparator.tier.06" -> new int[]{7, 0};
            case "industrialcokeoven.controller.tier.single", "multimachine.pyro", "multimachine.vacuumfreezer", "multimachine.adv.industrialfreezer" ->
                new int[]{8, 0};
            case "multimachine.assemblyline" -> new int[]{9, 0};
            case "industrialsalloyamelter.controller.tier.single" -> new int[]{10, 0};
            case "moleculartransformer.controller.tier.single", "gtpp.multimachine.replicator" -> new int[]{0, 1};
            case "preciseassembler" -> new int[]{1, 1};
            case "chemicalplant.controller.tier.single" -> new int[]{2, 1};
            case "cyclotron.tier.single" -> new int[]{3, 1};
            case "multimachine.pcbfactory", "circuitassemblyline" -> new int[]{4, 1};
            case "largefusioncomputer5" -> new int[]{5, 1};
            case "dissolution_tank", "bw.biovat" -> new int[]{7, 1};
            case "electricimplosioncompressor", "componentassemblyline", "projectmoduleassemblert3" -> new int[]{9, 1};
            case "multimachine.plasmaforge", "multimachine.transcendentplasmamixer", "multimachine.nanoforge" ->
                new int[]{8, 1};
            case "quantumforcetransformer.controller.tier.single", "frf", "industrialmassfab.controller.tier.single" ->
                new int[]{10, 1};
            default -> new int[]{14, 0};
        };
    }
}

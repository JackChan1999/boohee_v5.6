package com.google.zxing.maxicode.decoder;

import android.support.v4.view.InputDeviceCompat;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.boohee.uploader.QiniuConfig;
import com.google.zxing.common.BitMatrix;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qiniu.android.dns.Record;
import com.tencent.tinker.android.dx.instruction.Opcodes;
import com.umeng.a;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.SocializeConstants;
import com.wdullaer.materialdatetimepicker.Utils;

import org.java_websocket.WebSocket;

final class BitMatrixParser {
    private static final int[][] BITNR = new int[][]{new int[]{SampleTinkerReport
            .KEY_APPLIED_DEXOPT, 120, 127, 126, Opcodes.LONG_TO_FLOAT, 132, 139, 138, 145, 144,
            151, 150, 157, 156, 163, 162, 169, 168, 175, Opcodes.DIV_DOUBLE, 181, 180, 187,
            Opcodes.USHR_INT_2ADDR, 193, 192, 199, 198, -2, -2}, new int[]{Opcodes.NEG_INT, 122,
            Opcodes.INT_TO_LONG, 128, Opcodes.FLOAT_TO_INT, Opcodes.LONG_TO_DOUBLE, 141, 140,
            147, 146, 153, 152, 159, 158, 165, 164, Opcodes.ADD_DOUBLE, Opcodes.REM_FLOAT, 177,
            176, 183, 182, Opcodes.MUL_LONG_2ADDR, Opcodes.SUB_LONG_2ADDR, 195, Opcodes
            .XOR_LONG_2ADDR, 201, 200, 816, -3}, new int[]{Opcodes.NEG_LONG, Opcodes.NOT_INT,
            Opcodes.INT_TO_DOUBLE, 130, 137, Opcodes.FLOAT_TO_LONG, Opcodes.INT_TO_SHORT, 142,
            149, 148, 155, 154, 161, 160, 167, 166, Opcodes.MUL_DOUBLE, 172, 179, 178, 185, 184,
            Opcodes.REM_LONG_2ADDR, Opcodes.DIV_LONG_2ADDR, Opcodes.USHR_LONG_2ADDR, Opcodes
            .SHR_LONG_2ADDR, 203, 202, 818, 817}, new int[]{283, 282, 277, 276, 271, 270, 265,
            264, 259, 258, SampleTinkerReport.KEY_LOADED_EXCEPTION_DEX_CHECK, SampleTinkerReport
            .KEY_LOADED_EXCEPTION_DEX, 247, 246, 241, SocializeConstants
            .MASK_USER_CENTER_HIDE_AREA, 235, 234, 229, 228, Opcodes.XOR_INT_LIT8, Opcodes
            .OR_INT_LIT8, Opcodes.RSUB_INT_LIT8, Opcodes.ADD_INT_LIT8, Opcodes.DIV_INT_LIT16,
            Opcodes.MUL_INT_LIT16, 205, 204, 819, -3}, new int[]{285, 284, 279, 278, 273, 272,
            267, 266, 261, 260, 255, 254, 249, 248, 243, 242, 237, 236, 231, 230, Opcodes
            .SHR_INT_LIT8, Opcodes.SHL_INT_LIT8, Opcodes.DIV_INT_LIT8, Opcodes.MUL_INT_LIT8,
            Opcodes.AND_INT_LIT16, Opcodes.REM_INT_LIT16, 207, 206, 821, 820}, new int[]{287,
            286, 281, 280, 275, 274, 269, 268, 263, 262, InputDeviceCompat.SOURCE_KEYBOARD, 256,
            SampleTinkerReport.KEY_LOADED_UNCAUGHT_EXCEPTION, 250, 245, 244, 239, 238, 233, 232,
            227, Opcodes.USHR_INT_LIT8, Opcodes.AND_INT_LIT8, Opcodes.REM_INT_LIT8, Opcodes
            .XOR_INT_LIT16, Opcodes.OR_INT_LIT16, 209, 208, 822, -3}, new int[]{289, 288, 295,
            294, SampleTinkerReport.KEY_LOADED_MISMATCH_LIB, 300, 307, SampleTinkerReport
            .KEY_LOADED_MISSING_PATCH_INFO, 313, 312, 319, 318, PullToRefreshBase
            .SMOOTH_SCROLL_LONG_DURATION_MS, 324, 331, 330, 337, 336, 343, 342, 349, 348,
            SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL, SampleTinkerReport
            .KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND, 361, 360, 367, 366, 824, 823},
            new int[]{291, 290, 297, 296, SampleTinkerReport.KEY_LOADED_MISSING_DEX,
                    SampleTinkerReport.KEY_LOADED_MISMATCH_RESOURCE, SampleTinkerReport
                    .KEY_LOADED_INFO_CORRUPTED, 308, 315, 314, 321, 320, 327, 326, 333, 332, 339,
                    338, 345, 344, SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_DEX_META,
                    SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_SIGNATURE, SampleTinkerReport
                    .KEY_LOADED_PACKAGE_CHECK_RES_META, SampleTinkerReport
                    .KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND, 363, 362, 369, 368, 825,
                    -3}, new int[]{293, 292, 299, 298, SampleTinkerReport
            .KEY_LOADED_MISSING_PATCH_FILE, SampleTinkerReport.KEY_LOADED_MISSING_LIB, 311, 310,
            317, 316, 323, 322, 329, 328, 335, 334, 341, 340, 347, 346, SampleTinkerReport
            .KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND, SampleTinkerReport
            .KEY_LOADED_PACKAGE_CHECK_LIB_META, 359, 358, 365, 364, 371, 370, 827, 826}, new
            int[]{409, 408, SampleTinkerReport.KEY_LOADED_SUCC_COST_5000_LESS, SampleTinkerReport
            .KEY_LOADED_SUCC_COST_3000_LESS, 397, 396, 391, 390, 79, 78, -2, -2, 13, 12, 37, 36,
            2, -1, 44, 43, 109, 108, 385, 384, 379, 378, 373, 372, 828, -3}, new int[]{411, 410,
            405, SampleTinkerReport.KEY_LOADED_SUCC_COST_OTHER, 399, 398, 393, 392, 81, 80, 40,
            -2, 15, 14, 39, 38, 3, -1, -1, 45, 111, 110, 387, 386, 381, 380, 375, 374, 830, 829},
            new int[]{413, 412, 407, 406, SampleTinkerReport.KEY_LOADED_SUCC_COST_1000_LESS, 400,
                    395, 394, 83, 82, 41, -3, -3, -3, -3, -3, 5, 4, 47, 46, Opcodes
                    .INVOKE_STATIC, 112, 389, 388, 383, 382, 377, 376, 831, -3}, new int[]{415,
            414, 421, 420, 427, 426, 103, 102, 55, 54, 16, -3, -3, -3, -3, -3, -3, -3, 20, 19,
            85, 84, 433, 432, 439, 438, 445, 444, 833, 832}, new int[]{417, 416, 423, 422, 429,
            428, 105, 104, 57, 56, -3, -3, -3, -3, -3, -3, -3, -3, 22, 21, 87, 86, 435, 434, 441,
            440, 447, 446, 834, -3}, new int[]{419, 418, 425, 424, 431, a.e, 107, 106, 59, 58,
            -3, -3, -3, -3, -3, -3, -3, -3, -3, 23, 89, 88, 437, 436, WebSocket.DEFAULT_WSS_PORT,
            442, 449, 448, 836, 835}, new int[]{481, QiniuConfig.MAX_WIDTH, 475, 474, 469, 468,
            48, -2, 30, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 0, 53, 52, 463, 462, 457, 456,
            451, 450, 837, -3}, new int[]{483, 482, 477, 476, 471, 470, 49, -1, -2, -3, -3, -3,
            -3, -3, -3, -3, -3, -3, -3, -3, -2, -1, 465, 464, 459, 458, 453, 452, 839, 838}, new
            int[]{485, 484, 479, 478, 473, 472, 51, 50, 31, -3, -3, -3, -3, -3, -3, -3, -3, -3,
            -3, 1, -2, 42, 467, 466, 461, 460, 455, 454, 840, -3}, new int[]{487, 486, 493, 492,
            499, 498, 97, 96, 61, 60, -3, -3, -3, -3, -3, -3, -3, -3, -3, 26, 91, 90, StatusCode
            .ST_CODE_USER_BANNED, 504, 511, 510, 517, 516, 842, 841}, new int[]{489, 488, 495,
            494, 501, 500, 99, 98, 63, 62, -3, -3, -3, -3, -3, -3, -3, -3, 28, 27, 93, 92, 507,
            506, InputDeviceCompat.SOURCE_DPAD, 512, 519, 518, 843, -3}, new int[]{491, 490, 497,
            496, 503, 502, 101, 100, 65, 64, 17, -3, -3, -3, -3, -3, -3, -3, 18, 29, 95, 94, 509,
            508, 515, 514, 521, 520, 845, 844}, new int[]{559, 558, 553, 552, 547, 546, 541, 540,
            73, 72, 32, -3, -3, -3, -3, -3, -3, 10, 67, 66, 115, Opcodes.INVOKE_INTERFACE, 535,
            534, 529, 528, 523, 522, 846, -3}, new int[]{561, 560, 555, 554, 549, 548, 543, 542,
            75, 74, -2, -1, 7, 6, 35, 34, 11, -2, 69, 68, Opcodes.INVOKE_SUPER_RANGE, Opcodes
            .INVOKE_VIRTUAL_RANGE, 537, 536, 531, 530, 525, 524, 848, 847}, new int[]{563, 562,
            557, 556, 551, 550, 545, Utils.PULSE_ANIMATOR_DURATION, 77, 76, -2, 33, 9, 8, 25, 24,
            -1, -2, 71, 70, Opcodes.INVOKE_STATIC_RANGE, Opcodes.INVOKE_DIRECT_RANGE, 539, 538,
            533, 532, 527, 526, 849, -3}, new int[]{565, 564, 571, 570, 577, 576, 583, 582, 589,
            588, 595, 594, 601, Record.TTL_MIN_SECONDS, 607, 606, 613, 612, 619, 618, 625, 624,
            631, 630, 637, 636, 643, 642, 851, 850}, new int[]{567, 566, 573, 572, 579, 578, 585,
            584, 591, 590, 597, 596, 603, 602, 609, 608, 615, 614, 621, 620, 627, 626, 633, 632,
            639, 638, 645, 644, 852, -3}, new int[]{569, 568, 575, 574, 581, 580, 587, 586, 593,
            592, 599, 598, 605, 604, 611, 610, 617, 616, 623, 622, 629, 628, 635, 634, 641, 640,
            647, 646, 854, 853}, new int[]{727, 726, 721, 720, 715, 714, 709, 708, 703, 702, 697,
            696, 691, 690, 685, 684, 679, 678, 673, 672, 667, 666, 661, 660, 655, 654, 649, 648,
            855, -3}, new int[]{729, 728, 723, 722, 717, 716, 711, 710, 705, 704, 699, 698, 693,
            692, 687, 686, 681, 680, 675, 674, 669, 668, 663, 662, 657, 656, 651, 650, 857, 856},
            new int[]{731, 730, 725, 724, 719, 718, 713, 712, 707, 706, 701, 700, 695, 694, 689,
                    688, 683, 682, 677, 676, 671, 670, 665, 664, 659, 658, 653, 652, 858, -3},
            new int[]{733, 732, 739, 738, 745, 744, 751, 750, 757, 756, 763, 762, 769, Opcodes
                    .FILL_ARRAY_DATA_PAYLOAD, 775, 774, 781, 780, 787, 786, 793, 792, 799, 798,
                    805, 804, 811, 810, 860, 859}, new int[]{735, 734, 741, 740, 747, 746, 753,
            752, 759, 758, 765, 764, 771, 770, 777, 776, 783, 782, 789, 788, 795, 794, 801, 800,
            807, 806, 813, 812, 861, -3}, new int[]{737, 736, 743, 742, 749, 748, 755, 754, 761,
            760, 767, 766, 773, 772, 779, 778, 785, 784, 791, 790, 797, 796, 803, 802, 809, 808,
            815, 814, 863, 862}};
    private final BitMatrix bitMatrix;

    BitMatrixParser(BitMatrix bitMatrix) {
        this.bitMatrix = bitMatrix;
    }

    byte[] readCodewords() {
        byte[] result = new byte[144];
        int height = this.bitMatrix.getHeight();
        int width = this.bitMatrix.getWidth();
        int y = 0;
        while (y < height) {
            int[] bitnrRow = BITNR[y];
            int x = 0;
            while (x < width) {
                int bit = bitnrRow[x];
                if (bit >= 0 && this.bitMatrix.get(x, y)) {
                    int i = bit / 6;
                    result[i] = (byte) (result[i] | ((byte) (1 << (5 - (bit % 6)))));
                }
                x++;
            }
            y++;
        }
        return result;
    }
}

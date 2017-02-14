package com.tencent.tinker.lib.patch;

import android.content.Context;
import android.os.SystemClock;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareDexDiffPatchInfo;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import dalvik.system.DexFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class DexDiffPatchInternal extends BasePatchInternal {
    protected static final String TAG = "Tinker.DexDiffPatchInternal";

    private static boolean extractDexDiffInternals(android.content.Context r46, java.lang.String
            r47, java.lang.String r48, java.io.File r49, int r50, boolean r51) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block
B:182:0x05a9 in {5, 33, 36, 48, 50, 52, 55, 56, 58, 60, 61, 68, 69, 76, 108, 110, 146, 148, 150,
154, 157, 167, 170, 176, 178, 180, 181, 183, 184, 185, 186, 187, 188, 189, 190, 191, 193, 196,
197, 198, 199, 200, 201, 203, 204, 205, 207, 208, 209, 211, 212, 213, 214, 215, 216, 217, 219,
222, 223, 224, 225, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243,
244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 257, 258, 259} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r32 = new java.util.ArrayList;
        r32.<init>();
        r0 = r48;
        r1 = r32;
        com.tencent.tinker.loader.shareutil.ShareDexDiffPatchInfo.parseDexDiffPatchInfo(r0, r1);
        r4 = r32.isEmpty();
        if (r4 == 0) goto L_0x0027;
    L_0x0012:
        r4 = "Tinker.DexDiffPatchInternal";
        r5 = "extract patch list is empty! type:%s:";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r9 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.getTypeString(r50);
        r7[r8] = r9;
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);
        r4 = 1;
    L_0x0026:
        return r4;
    L_0x0027:
        r15 = new java.io.File;
        r0 = r47;
        r15.<init>(r0);
        r4 = r15.exists();
        if (r4 != 0) goto L_0x0037;
    L_0x0034:
        r15.mkdirs();
    L_0x0037:
        r23 = com.tencent.tinker.lib.tinker.Tinker.with(r46);
        r10 = 0;
        r29 = 0;
        r13 = r46.getApplicationInfo();	 Catch:{ Throwable -> 0x0669 }
        if (r13 != 0) goto L_0x0058;	 Catch:{ Throwable -> 0x0669 }
    L_0x0044:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x0669 }
        r5 = "applicationInfo == null!!!!";	 Catch:{ Throwable -> 0x0669 }
        r7 = 0;	 Catch:{ Throwable -> 0x0669 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x0669 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x0669 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r10);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r29);
        goto L_0x0026;
    L_0x0058:
        r12 = r13.sourceDir;	 Catch:{ Throwable -> 0x0669 }
        r11 = new java.util.zip.ZipFile;	 Catch:{ Throwable -> 0x0669 }
        r11.<init>(r12);	 Catch:{ Throwable -> 0x0669 }
        r30 = new java.util.zip.ZipFile;	 Catch:{ Throwable -> 0x066c, all -> 0x0665 }
        r0 = r30;	 Catch:{ Throwable -> 0x066c, all -> 0x0665 }
        r1 = r49;	 Catch:{ Throwable -> 0x066c, all -> 0x0665 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x066c, all -> 0x0665 }
        r37 = 0;
        r4 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.isVmArt();	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        if (r4 == 0) goto L_0x00cd;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0070:
        r6 = new java.io.File;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r47;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = r4.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "smallpatch_info.ddextra";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = r4.append(r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = r4.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r6.<init>(r4);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = "smallpatch_info.ddextra";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r30;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r36 = r0.getEntry(r4);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r36 != 0) goto L_0x00bb;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0096:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "small patch info is not exists, bad patch package?";	 Catch:{ Throwable -> 0x018c,
        all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "smallpatch_info.ddextra";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x00bb:
        r38 = 0;
        r0 = r30;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r1 = r36;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r38 = r0.getInputStream(r1);	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r37 = new com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;	 Catch:{
        Throwable -> 0x0149, all -> 0x0187 }
        r37.<init>(r38);	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r38);
    L_0x00cd:
        r4 = r32.iterator();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x00d1:
        r5 = r4.hasNext();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x065c;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x00d7:
        r20 = r4.next();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r20 = (com.tencent.tinker.loader.shareutil.ShareDexDiffPatchInfo) r20;	 Catch:{ Throwable
         -> 0x018c, all -> 0x0237 }
        r40 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r0.path;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r21 = r0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r21;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r0.equals(r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x01ce;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x00f2:
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r33 = r0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x00f8:
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r14 = r0.dexDiffMd5;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r0.oldDexCrC;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r25 = r0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.isVmArt();	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x01f0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0108:
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r0.destMd5InArt;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r18 = r0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x010e:
        r5 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.checkIfMd5Valid(r18);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x01f8;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0114:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "meta file md5 invalid, type:%s, name: %s, md5: %s";	 Catch:{ Throwable -> 0x018c,
        all -> 0x0237 }
        r7 = 3;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.getTypeString(r50);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r9;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r9;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 2;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r18;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.lib.patch.BasePatchInternal.getMetaCorruptedCode(r50);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r0 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchPackageCheckFail(r0, r1, r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x0149:
        r16 = move-exception;
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r5.<init>();	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r7 = "failed to read small patched info. reason: ";	 Catch:{ Throwable -> 0x0149, all ->
        0x0187 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r7 = r16.getMessage();	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r7 = 0;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x0149, all
        -> 0x0187 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r7 = "smallpatch_info.ddextra";	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r5 = r49;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r8 = r50;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r9 = r51;	 Catch:{ Throwable -> 0x0149, all -> 0x0187 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x0149, all ->
        0x0187 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r38);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x0187:
        r4 = move-exception;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r38);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        throw r4;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x018c:
        r16 = move-exception;
        r29 = r30;
        r10 = r11;
    L_0x0190:
        r4 = new com.tencent.tinker.loader.TinkerRuntimeException;	 Catch:{ all -> 0x01c6 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01c6 }
        r5.<init>();	 Catch:{ all -> 0x01c6 }
        r7 = "patch ";	 Catch:{ all -> 0x01c6 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x01c6 }
        r7 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.getTypeString(r50);
        Catch:{ all -> 0x01c6 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x01c6 }
        r7 = " extract failed (";	 Catch:{ all -> 0x01c6 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x01c6 }
        r7 = r16.getMessage();	 Catch:{ all -> 0x01c6 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x01c6 }
        r7 = ").";	 Catch:{ all -> 0x01c6 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x01c6 }
        r5 = r5.toString();	 Catch:{ all -> 0x01c6 }
        r0 = r16;	 Catch:{ all -> 0x01c6 }
        r4.<init>(r5, r0);	 Catch:{ all -> 0x01c6 }
        throw r4;	 Catch:{ all -> 0x01c6 }
    L_0x01c6:
        r4 = move-exception;
    L_0x01c7:
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r10);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r29);
        throw r4;
    L_0x01ce:
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.path;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "/";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r33 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        goto L_0x00f8;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x01f0:
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r0.destMd5InDvm;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r18 = r0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        goto L_0x010e;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x01f8:
        r6 = new java.io.File;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r47;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.realName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r6.<init>(r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r6.exists();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x02b4;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x021a:
        r0 = r18;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.verifyDexFileMd5(r6, r0);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x023c;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0222:
        r5 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "dex file %s is already exist, and md5 match, just continue";	 Catch:{ Throwable ->
        0x018c, all -> 0x0237 }
        r8 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r43 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8[r9] = r43;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r5, r7, r8);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        goto L_0x00d1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0237:
        r4 = move-exception;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r29 = r30;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r10 = r11;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        goto L_0x01c7;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x023c:
        r5 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = "have a mismatch corrupted dex ";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r7.append(r8);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r7.append(r8);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r5, r7, r8);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r6.delete();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0260:
        r0 = r30;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r31 = r0.getEntry(r1);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r34 = r11.getEntry(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "0";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r25;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r0.equals(r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x0303;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0279:
        if (r31 != 0) goto L_0x02bc;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x027b:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "patch entry is null. path:";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x02b4:
        r5 = r6.getParentFile();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.mkdirs();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        goto L_0x0260;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x02bc:
        r0 = r30;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r31;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r2 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = extractDexFile(r0, r1, r6, r2);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x00d1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x02c8:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "Failed to extract raw patch file ";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x0303:
        r5 = "0";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r14.equals(r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x0438;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x030c:
        r5 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.isVmArt();	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        if (r5 == 0) goto L_0x00d1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0312:
        if (r34 != 0) goto L_0x034d;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0314:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "apk entry is null. path:";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x034d:
        r8 = r34.getCrc();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r35 = java.lang.String.valueOf(r8);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r35;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r25;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r0.equals(r1);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x038e;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x035f:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "apk entry %s crc is not equal, expect crc: %s, got crc: %s";	 Catch:{ Throwable ->
        0x018c, all -> 0x0237 }
        r7 = 3;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r25;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 2;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r35;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.e(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x038e:
        r26 = 0;
        r0 = r34;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r26 = r11.getInputStream(r0);	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = new com.tencent.tinker.commons.dexpatcher.DexPatchApplier;	 Catch:{ Throwable ->
        0x03f1, all -> 0x0433 }
        r8 = r34.getSize();	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = (int) r8;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r8 = 0;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r0 = r26;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r1 = r37;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5.<init>(r0, r7, r8, r1);	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5.executeAndSaveTo(r6);	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r26);
        r0 = r18;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.verifyDexFileMd5(r6, r0);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x00d1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x03b3:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "Failed to recover dex file ";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.safeDeleteFile(r6);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x03f1:
        r16 = move-exception;
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5.<init>();	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = "Failed to recover dex file ";	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = r6.getPath();	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = 0;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x03f1, all
        -> 0x0433 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r0 = r20;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r5 = r49;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r8 = r50;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r9 = r51;	 Catch:{ Throwable -> 0x03f1, all -> 0x0433 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x03f1, all ->
        0x0433 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.safeDeleteFile(r6);	 Catch:{
        Throwable -> 0x03f1, all -> 0x0433 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r26);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x0433:
        r4 = move-exception;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r26);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        throw r4;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0438:
        if (r31 != 0) goto L_0x0473;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x043a:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "patch entry is null. path:";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x0473:
        r5 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.checkIfMd5Valid(r14);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x04ae;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x0479:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "meta file md5 invalid, type:%s, name: %s, md5: %s";	 Catch:{ Throwable -> 0x018c,
        all -> 0x0237 }
        r7 = 3;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = com.tencent.tinker.loader.shareutil.ShareTinkerInternals.getTypeString(r50);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r9;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r9;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 2;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r14;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.lib.patch.BasePatchInternal.getMetaCorruptedCode(r50);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r0 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchPackageCheckFail(r0, r1, r5);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x04ae:
        if (r34 != 0) goto L_0x04e9;
    L_0x04b0:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "apk entry is null. path:";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x04e9:
        r8 = r34.getCrc();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r35 = java.lang.String.valueOf(r8);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r35;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r1 = r25;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r0.equals(r1);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x052a;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x04fb:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = "apk entry %s crc is not equal, expect crc: %s, got crc: %s";	 Catch:{ Throwable ->
        0x018c, all -> 0x0237 }
        r7 = 3;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r33;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r25;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = 2;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7[r8] = r35;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.e(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x052a:
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r22 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.isRawDexFile(r5);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r0 = r34;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r28 = r11.getInputStream(r0);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r24 = r30.getInputStream(r31);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r22 == 0) goto L_0x0544;
    L_0x053e:
        r0 = r20;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = r0.isJarMode;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        if (r5 == 0) goto L_0x0626;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0544:
        r19 = new java.io.FileOutputStream;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r19;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0.<init>(r6);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r42 = new java.util.zip.ZipOutputStream;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r19;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.<init>(r0);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r42;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0.<init>(r5);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = new java.util.zip.ZipEntry;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r7 = "classes.dex";	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.<init>(r7);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r42;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0.putNextEntry(r5);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        if (r22 != 0) goto L_0x061e;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0568:
        r39 = new java.util.zip.ZipInputStream;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r39;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r1 = r28;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0.<init>(r1);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0571:
        r17 = r39.getNextEntry();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        if (r17 == 0) goto L_0x0584;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0577:
        r5 = "classes.dex";	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r7 = r17.getName();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = r5.equals(r7);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        if (r5 == 0) goto L_0x0571;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0584:
        if (r17 != 0) goto L_0x05b1;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x0586:
        r4 = new com.tencent.tinker.loader.TinkerRuntimeException;	 Catch:{ all -> 0x05a4, all ->
         0x05a9 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.<init>();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r7 = "can't recognize zip dex format file:";	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r7 = r6.getAbsolutePath();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5 = r5.toString();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r4.<init>(r5);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        throw r4;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x05a4:
        r4 = move-exception;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r42);	 Catch:{ all
        -> 0x05a4, all -> 0x05a9 }
        throw r4;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x05a9:
        r4 = move-exception;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r28);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r24);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        throw r4;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x05b1:
        r28 = r39;
        r8 = r17.getSize();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = (int) r8;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r27 = r0;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
    L_0x05ba:
        r5 = new com.tencent.tinker.commons.dexpatcher.DexPatchApplier;	 Catch:{ all -> 0x05a4,
        all -> 0x05a9 }
        r0 = r28;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r1 = r27;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r2 = r24;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r3 = r37;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.<init>(r0, r1, r2, r3);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r42;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.executeAndSaveTo(r0);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r42.closeEntry();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r42);
    L_0x05d2:
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r28);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeQuietly(r24);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r0 = r18;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = com.tencent.tinker.loader.shareutil.SharePatchFileUtil.verifyDexFileMd5(r6, r0);
        Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        if (r5 != 0) goto L_0x063a;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
    L_0x05e0:
        r4 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5.<init>();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "Failed to recover dex file ";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r4, r5, r7);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        r4 = r23.getPatchReporter();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r0 = r20;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = r0.rawName;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r5 = r49;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = r50;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = r51;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r4.onPatchTypeExtractFail(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.safeDeleteFile(r6);	 Catch:{
        Throwable -> 0x018c, all -> 0x0237 }
        r4 = 0;
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        goto L_0x0026;
    L_0x061e:
        r8 = r34.getSize();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = (int) r8;
        r27 = r0;
        goto L_0x05ba;
    L_0x0626:
        r5 = new com.tencent.tinker.commons.dexpatcher.DexPatchApplier;	 Catch:{ all -> 0x05a4,
        all -> 0x05a9 }
        r8 = r34.getSize();	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r7 = (int) r8;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r0 = r28;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r1 = r24;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r2 = r37;	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.<init>(r0, r7, r1, r2);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        r5.executeAndSaveTo(r6);	 Catch:{ all -> 0x05a4, all -> 0x05a9 }
        goto L_0x05d2;
    L_0x063a:
        r5 = "Tinker.DexDiffPatchInternal";	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r7 = "success recover dex file: %s, use time: %d";	 Catch:{ Throwable -> 0x018c, all ->
        0x0237 }
        r8 = 2;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = 0;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r43 = r6.getPath();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8[r9] = r43;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r9 = 1;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r44 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r44 = r44 - r40;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r43 = java.lang.Long.valueOf(r44);	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        r8[r9] = r43;	 Catch:{ Throwable -> 0x018c, all -> 0x0237 }
        com.tencent.tinker.lib.util.TinkerLog.w(r5, r7, r8);	 Catch:{ Throwable -> 0x018c, all
        -> 0x0237 }
        goto L_0x00d1;
    L_0x065c:
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r11);
        com.tencent.tinker.loader.shareutil.SharePatchFileUtil.closeZip(r30);
        r4 = 1;
        goto L_0x0026;
    L_0x0665:
        r4 = move-exception;
        r10 = r11;
        goto L_0x01c7;
    L_0x0669:
        r16 = move-exception;
        goto L_0x0190;
    L_0x066c:
        r16 = move-exception;
        r10 = r11;
        goto L_0x0190;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.tinker.lib" +
                ".patch.DexDiffPatchInternal.extractDexDiffInternals(android.content.Context, " +
                "java.lang.String, java.lang.String, java.io.File, int, boolean):boolean");
    }

    protected static boolean tryRecoverDexFiles(Tinker manager, ShareSecurityCheck checker,
                                                Context context, String patchVersionDirectory,
                                                File patchFile, boolean isUpgradePatch) {
        if (manager.isEnabledForDex()) {
            String dexMeta = (String) checker.getMetaContentMap().get(ShareConstants.DEX_META_FILE);
            if (dexMeta == null) {
                TinkerLog.w(TAG, "patch recover, dex is not contained", new Object[0]);
                return true;
            }
            long begin = SystemClock.elapsedRealtime();
            long cost = SystemClock.elapsedRealtime() - begin;
            TinkerLog.i(TAG, "recover dex result:%b, cost:%d, isUpgradePatch:%b", Boolean.valueOf
                    (patchDexExtractViaDexDiff(context, patchVersionDirectory, dexMeta,
                            patchFile, isUpgradePatch)), Long.valueOf(cost), Boolean.valueOf
                    (isUpgradePatch));
            return patchDexExtractViaDexDiff(context, patchVersionDirectory, dexMeta, patchFile,
                    isUpgradePatch);
        }
        TinkerLog.w(TAG, "patch recover, dex is not enabled", new Object[0]);
        return true;
    }

    private static boolean patchDexExtractViaDexDiff(Context context, String
            patchVersionDirectory, String meta, File patchFile, boolean isUpgradePatch) {
        String dir = patchVersionDirectory + "/" + ShareConstants.DEX_PATH + "/";
        if (extractDexDiffInternals(context, dir, meta, patchFile, ShareTinkerInternals.isVmArt()
                ? 4 : 3, isUpgradePatch)) {
            Tinker manager = Tinker.with(context);
            File[] files = new File(dir).listFiles();
            if (files != null) {
                String optimizeDexDirectory = patchVersionDirectory + "/" + ShareConstants
                        .DEX_OPTIMIZE_PATH + "/";
                File file = new File(optimizeDexDirectory);
                if (!file.exists()) {
                    file.mkdirs();
                }
                int length = files.length;
                int i = 0;
                while (i < length) {
                    File file2 = files[i];
                    try {
                        String outputPathName = SharePatchFileUtil.optimizedPathFor(file2, file);
                        long start = System.currentTimeMillis();
                        DexFile.loadDex(file2.getAbsolutePath(), outputPathName, 0);
                        TinkerLog.i(TAG, "success dex optimize file, path: %s, use time: %d",
                                file2.getPath(), Long.valueOf(System.currentTimeMillis() - start));
                        i++;
                    } catch (Throwable e) {
                        TinkerLog.e(TAG, "dex optimize or load failed, path:" + file2.getPath(),
                                new Object[0]);
                        SharePatchFileUtil.safeDeleteFile(file2);
                        manager.getPatchReporter().onPatchDexOptFail(patchFile, file2,
                                optimizeDexDirectory, file2.getName(), e, isUpgradePatch);
                        return false;
                    }
                }
            }
            return true;
        }
        TinkerLog.w(TAG, "patch recover, extractDiffInternals fail", new Object[0]);
        return false;
    }

    private static boolean extractDexToJar(ZipFile zipFile, ZipEntry entryFile, File extractTo,
                                           String targetMd5) throws IOException {
        Throwable th;
        int numAttempts = 0;
        boolean isExtractionSuccessful = false;
        while (numAttempts < 2 && !isExtractionSuccessful) {
            numAttempts++;
            FileOutputStream fos = new FileOutputStream(extractTo);
            InputStream in = zipFile.getInputStream(entryFile);
            ZipOutputStream zos = null;
            BufferedInputStream bis = null;
            TinkerLog.i(TAG, "try Extracting " + extractTo.getPath(), new Object[0]);
            try {
                ZipOutputStream zos2 = new ZipOutputStream(new BufferedOutputStream(fos));
                try {
                    BufferedInputStream bis2 = new BufferedInputStream(in);
                    try {
                        byte[] buffer = new byte[16384];
                        zos2.putNextEntry(new ZipEntry("classes.dex"));
                        for (int length = bis2.read(buffer); length != -1; length = bis2.read
                                (buffer)) {
                            zos2.write(buffer, 0, length);
                        }
                        zos2.closeEntry();
                        SharePatchFileUtil.closeQuietly(bis2);
                        SharePatchFileUtil.closeQuietly(zos2);
                        isExtractionSuccessful = SharePatchFileUtil.verifyDexFileMd5(extractTo,
                                targetMd5);
                        TinkerLog.i(TAG, "isExtractionSuccessful: %b", Boolean.valueOf
                                (isExtractionSuccessful));
                        if (!isExtractionSuccessful) {
                            extractTo.delete();
                            if (extractTo.exists()) {
                                TinkerLog.e(TAG, "Failed to delete corrupted dex " + extractTo
                                        .getPath(), new Object[0]);
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bis = bis2;
                        zos = zos2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    zos = zos2;
                }
            } catch (Throwable th4) {
                th = th4;
            }
        }
        return isExtractionSuccessful;
        SharePatchFileUtil.closeQuietly(bis);
        SharePatchFileUtil.closeQuietly(zos);
        throw th;
    }

    private static boolean extractDexFile(ZipFile zipFile, ZipEntry entryFile, File extractTo,
                                          ShareDexDiffPatchInfo dexInfo) throws IOException {
        String fileMd5 = ShareTinkerInternals.isVmArt() ? dexInfo.destMd5InArt : dexInfo
                .destMd5InDvm;
        String rawName = dexInfo.rawName;
        boolean isJarMode = dexInfo.isJarMode;
        if (SharePatchFileUtil.isRawDexFile(rawName) && isJarMode) {
            return extractDexToJar(zipFile, entryFile, extractTo, fileMd5);
        }
        return BasePatchInternal.extract(zipFile, entryFile, extractTo, fileMd5, true);
    }
}

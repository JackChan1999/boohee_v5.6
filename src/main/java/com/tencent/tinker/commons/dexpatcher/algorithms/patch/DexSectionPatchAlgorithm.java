package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.SizeOf;
import com.tencent.tinker.android.dex.TableOfContents.Section;
import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.Hex;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;

import java.util.Arrays;

public abstract class DexSectionPatchAlgorithm<T extends Comparable<T>> {
    private final   IndexMap                   fullPatchedToSmallPatchedIndexMap;
    protected final Dex                        oldDex;
    private final   String                     oldDexSignStr;
    private final   IndexMap                   oldToFullPatchedIndexMap;
    protected final DexPatchFile               patchFile;
    private         SmallPatchedDexItemChooser smallPatchedDexItemChooser;

    public interface SmallPatchedDexItemChooser {
        boolean isPatchedItemInSmallPatchedDex(String str, int i);
    }

    protected abstract int getFullPatchSectionBase();

    protected abstract int getItemSize(T t);

    protected abstract Section getTocSection(Dex dex);

    protected abstract T nextItem(DexDataBuffer dexDataBuffer);

    protected abstract int writePatchedItem(T t);

    public DexSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, IndexMap
            oldToFullPatchedIndexMap, IndexMap fullPatchedToSmallPatchedIndexMap) {
        this(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap, null);
    }

    public DexSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, IndexMap
            oldToFullPatchedIndexMap, IndexMap fullPatchedToSmallPatchedIndexMap,
                                    SmallPatchedDexItemChooser smallPatchedDexItemChooser) {
        this.smallPatchedDexItemChooser = null;
        this.patchFile = patchFile;
        this.oldDex = oldDex;
        this.oldToFullPatchedIndexMap = oldToFullPatchedIndexMap;
        this.fullPatchedToSmallPatchedIndexMap = fullPatchedToSmallPatchedIndexMap;
        this.oldDexSignStr = Hex.toHexString(oldDex.computeSignature(false));
        this.smallPatchedDexItemChooser = smallPatchedDexItemChooser;
    }

    protected T adjustItem(IndexMap indexMap, T item) {
        return item;
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
    }

    protected final boolean isPatchedItemInSmallPatchedDex(String oldDexSignStr, int patchedIndex) {
        if (this.smallPatchedDexItemChooser != null) {
            return this.smallPatchedDexItemChooser.isPatchedItemInSmallPatchedDex(oldDexSignStr,
                    patchedIndex);
        }
        return true;
    }

    private int[] readDeltaIndiciesOrOffsets(int count) {
        int[] result = new int[count];
        int lastVal = 0;
        for (int i = 0; i < count; i++) {
            lastVal += this.patchFile.getBuffer().readSleb128();
            result[i] = lastVal;
        }
        return result;
    }

    private int getItemOffsetOrIndex(int index, T item) {
        if (item instanceof Item) {
            return ((Item) item).off;
        }
        return index;
    }

    public void execute() {
        int[] deletedIndices;
        int[] addedIndices;
        int[] replacedIndices;
        if (this.patchFile != null) {
            deletedIndices = readDeltaIndiciesOrOffsets(this.patchFile.getBuffer().readUleb128());
            addedIndices = readDeltaIndiciesOrOffsets(this.patchFile.getBuffer().readUleb128());
            replacedIndices = readDeltaIndiciesOrOffsets(this.patchFile.getBuffer().readUleb128());
        } else {
            deletedIndices = new int[0];
            addedIndices = new int[0];
            replacedIndices = new int[0];
        }
        Section tocSec = getTocSection(this.oldDex);
        Dex.Section oldSection = null;
        int oldItemCount = 0;
        if (tocSec.exists()) {
            oldSection = this.oldDex.openSection(tocSec);
            oldItemCount = tocSec.size;
        }
        if (this.fullPatchedToSmallPatchedIndexMap == null) {
            doFullPatch(oldSection, oldItemCount, deletedIndices, addedIndices, replacedIndices);
        } else {
            doSmallPatch(oldSection, oldItemCount, deletedIndices, addedIndices, replacedIndices);
        }
    }

    private void doFullPatch(Dex.Section oldSection, int oldItemCount, int[] deletedIndices,
                             int[] addedIndices, int[] replacedIndices) {
        int deletedItemCount = deletedIndices.length;
        int addedItemCount = addedIndices.length;
        int replacedItemCount = replacedIndices.length;
        int newItemCount = (oldItemCount + addedItemCount) - deletedItemCount;
        int deletedItemCounter = 0;
        int addActionCursor = 0;
        int replaceActionCursor = 0;
        int oldIndex = 0;
        int patchedIndex = 0;
        while (true) {
            if (oldIndex >= oldItemCount && patchedIndex >= newItemCount) {
                break;
            } else if (addActionCursor < addedItemCount && addedIndices[addActionCursor] ==
                    patchedIndex) {
                patchedOffset = writePatchedItem(nextItem(this.patchFile.getBuffer()));
                addActionCursor++;
                patchedIndex++;
            } else if (replaceActionCursor < replacedItemCount &&
                    replacedIndices[replaceActionCursor] == patchedIndex) {
                patchedOffset = writePatchedItem(nextItem(this.patchFile.getBuffer()));
                replaceActionCursor++;
                patchedIndex++;
            } else if (Arrays.binarySearch(deletedIndices, oldIndex) >= 0) {
                T skippedOldItem = nextItem(oldSection);
                oldIndex++;
                deletedItemCounter++;
            } else if (Arrays.binarySearch(replacedIndices, oldIndex) >= 0) {
                Comparable nextItem = nextItem(oldSection);
                oldIndex++;
            } else if (oldIndex < oldItemCount) {
                T oldItem = adjustItem(this.oldToFullPatchedIndexMap, nextItem(oldSection));
                updateIndexOrOffset(this.oldToFullPatchedIndexMap, oldIndex, getItemOffsetOrIndex
                        (oldIndex, oldItem), patchedIndex, writePatchedItem(oldItem));
                oldIndex++;
                patchedIndex++;
            }
        }
        if (addActionCursor != addedItemCount || deletedItemCounter != deletedItemCount ||
                replaceActionCursor != replacedItemCount) {
            throw new IllegalStateException(String.format("bad patch operation sequence. " +
                    "addCounter: %d, addCount: %d, delCounter: %d, delCount: %d, replaceCounter: " +
                    "%d, replaceCount:%d", new Object[]{Integer.valueOf(addActionCursor), Integer
                    .valueOf(addedItemCount), Integer.valueOf(deletedItemCounter), Integer
                    .valueOf(deletedItemCount), Integer.valueOf(replaceActionCursor), Integer
                    .valueOf(replacedItemCount)}));
        }
    }

    private void doSmallPatch(Dex.Section oldSection, int oldItemCount, int[] deletedIndices,
                              int[] addedIndices, int[] replacedIndices) {
        int deletedItemCount = deletedIndices.length;
        int addedItemCount = addedIndices.length;
        int replacedItemCount = replacedIndices.length;
        int newItemCount = (oldItemCount + addedItemCount) - deletedItemCount;
        int deletedItemCounter = 0;
        int addActionCursor = 0;
        int replaceActionCursor = 0;
        int oldIndex = 0;
        int fullPatchedIndex = 0;
        int fullPatchedOffset = getFullPatchSectionBase();
        int smallPatchedIndex = 0;
        while (true) {
            if (oldIndex >= oldItemCount && fullPatchedIndex >= newItemCount) {
                break;
            } else if (addActionCursor < addedItemCount && addedIndices[addActionCursor] ==
                    fullPatchedIndex) {
                T addedItem = nextItem(this.patchFile.getBuffer());
                addActionCursor++;
                if (getTocSection(this.oldDex).isElementFourByteAligned) {
                    fullPatchedOffset = SizeOf.roundToTimesOfFour(fullPatchedOffset);
                }
                if (isPatchedItemInSmallPatchedDex(this.oldDexSignStr, fullPatchedIndex)) {
                    updateIndexOrOffset(this.fullPatchedToSmallPatchedIndexMap, fullPatchedIndex,
                            fullPatchedOffset, smallPatchedIndex, writePatchedItem(adjustItem
                                    (this.fullPatchedToSmallPatchedIndexMap, addedItem)));
                    smallPatchedIndex++;
                }
                fullPatchedIndex++;
                fullPatchedOffset += getItemSize(addedItem);
            } else if (replaceActionCursor < replacedItemCount &&
                    replacedIndices[replaceActionCursor] == fullPatchedIndex) {
                T replacedItem = nextItem(this.patchFile.getBuffer());
                replaceActionCursor++;
                if (getTocSection(this.oldDex).isElementFourByteAligned) {
                    fullPatchedOffset = SizeOf.roundToTimesOfFour(fullPatchedOffset);
                }
                if (isPatchedItemInSmallPatchedDex(this.oldDexSignStr, fullPatchedIndex)) {
                    updateIndexOrOffset(this.fullPatchedToSmallPatchedIndexMap, fullPatchedIndex,
                            fullPatchedOffset, smallPatchedIndex, writePatchedItem(adjustItem
                                    (this.fullPatchedToSmallPatchedIndexMap, replacedItem)));
                    smallPatchedIndex++;
                }
                fullPatchedIndex++;
                fullPatchedOffset += getItemSize(replacedItem);
            } else if (Arrays.binarySearch(deletedIndices, oldIndex) >= 0) {
                T skippedOldItem = nextItem(oldSection);
                oldIndex++;
                deletedItemCounter++;
            } else if (Arrays.binarySearch(replacedIndices, oldIndex) >= 0) {
                Comparable nextItem = nextItem(oldSection);
                oldIndex++;
            } else if (oldIndex < oldItemCount) {
                T oldItem = nextItem(oldSection);
                T oldItemInFullPatch = adjustItem(this.oldToFullPatchedIndexMap, oldItem);
                if (getTocSection(this.oldDex).isElementFourByteAligned) {
                    fullPatchedOffset = SizeOf.roundToTimesOfFour(fullPatchedOffset);
                }
                if (isPatchedItemInSmallPatchedDex(this.oldDexSignStr, fullPatchedIndex)) {
                    updateIndexOrOffset(this.fullPatchedToSmallPatchedIndexMap, fullPatchedIndex,
                            fullPatchedOffset, smallPatchedIndex, writePatchedItem(adjustItem
                                    (this.fullPatchedToSmallPatchedIndexMap, oldItemInFullPatch)));
                    smallPatchedIndex++;
                }
                updateIndexOrOffset(this.oldToFullPatchedIndexMap, oldIndex, getItemOffsetOrIndex
                        (oldIndex, oldItem), fullPatchedIndex, fullPatchedOffset);
                fullPatchedIndex++;
                fullPatchedOffset += getItemSize(oldItemInFullPatch);
                oldIndex++;
            }
        }
        if (addActionCursor != addedItemCount || deletedItemCounter != deletedItemCount ||
                replaceActionCursor != replacedItemCount) {
            throw new IllegalStateException(String.format("bad patch operation sequence. " +
                    "addCounter: %d, addCount: %d, delCounter: %d, delCount: %d, replaceCounter: " +
                    "%d, replaceCount:%d", new Object[]{Integer.valueOf(addActionCursor), Integer
                    .valueOf(addedItemCount), Integer.valueOf(deletedItemCounter), Integer
                    .valueOf(deletedItemCount), Integer.valueOf(replaceActionCursor), Integer.valueOf(replacedItemCount)}));
        }
    }
}

package com.zhs.imgselect.select;

import com.zhs.imgselect.util.SdcardUtils;

/**
 *
 * 初始化文件夹
 * Created by kuyue on 2017/6/20 下午3:38.
 * 邮箱:595327086@qq.com
 */

public class InitCacheFileUtils {

    /**
     * 初始化缓存目录，一般为两级，所以不考虑三级的情况
     * @param dirFileName
     * @param imgFileName
     */
    public static void initImgDir(String dirFileName, String imgFileName) {
        SdcardUtils sdcardUtils = new SdcardUtils();
        //创建文件夹
        if (SdcardUtils.existSdcard()) {//存在sd
            if (!sdcardUtils.isFileExist(dirFileName)) {
                //创建主目录
                sdcardUtils.creatSDDir(dirFileName);
            }
            if (sdcardUtils.fileExit(sdcardUtils.getSDPATH() + dirFileName + "/" + imgFileName)) {
                sdcardUtils.deleteFolder(sdcardUtils.getSDPATH() + dirFileName + "/" + imgFileName);
            }
            //创建目录
            sdcardUtils.creatFileDir(sdcardUtils.getSDPATH() + dirFileName, imgFileName);
        }
    }

}

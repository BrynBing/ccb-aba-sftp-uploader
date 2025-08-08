package com.ccb.aba.sftp.uploader;

import com.ccb.aba.sftp.uploader.controller.FlowController;
import com.ccb.aba.sftp.uploader.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {
        LocalDate startDate;
        LocalDate endDate;

        if (args.length == 0) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if (args.length == 2) {
            startDate = DateUtil.parseDate(args[0]);
            endDate = DateUtil.parseDate(args[1]);
        } else {
            System.out.println("Usage: STARTDATE<yyyymmdd> ENDDATE<yyyymmdd>");
            System.exit(1);
            return;
        }

        FlowController flowController = new FlowController();
        String timestamp = new java.util.Date().toString();
        System.out.println("[" + timestamp + "] Starting execution...");
        System.out.println(startDate + " - " + endDate);
        flowController.execute(startDate, endDate);
    }
}
package com.heaven7.java.machine.learning.test;

import com.heaven7.java.base.util.IOUtils;
import com.heaven7.java.base.util.Throwables;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GetReadCountTest {

    @Test
    public void getReadCount(){
        String fastqFile = "/home/heaven7/heaven7/work/shengxin/bwa/test_data/sample/" +
                "gatk-examples_example1_NA19913_ERR250256_1.filt.fastq.gz";

        BufferedReader r = null;
        try {
            if(fastqFile.endsWith(".gz")){
                r = new BufferedReader(new InputStreamReader(
                        new GZIPInputStream(new FileInputStream(fastqFile))));
            }else{
                r = new BufferedReader(new FileReader(fastqFile));
            }
            String line = null;
            int lineCount = 0;
            while ((line = r.readLine()) != null){
                lineCount ++;
                System.out.println(line);
            }
            System.out.println();
            System.out.println(">>> lineCount = " + lineCount + " ,read_count = " + lineCount / 4);
            Throwables.checkArgument(lineCount % 4 == 0, "lineCount must be 4n");
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(r);
        }
    }
}

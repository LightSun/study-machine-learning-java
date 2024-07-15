package com.heaven7.java.machine.learning.test;

import org.biojava.nbio.genome.io.fastq.Fastq;
import org.biojava.nbio.genome.io.fastq.IlluminaFastqReader;
import org.biojava.nbio.genome.io.fastq.SangerFastqReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class FastQTest {

    public static void main(String[] args) throws Exception{
        String str = "/media/heaven7/Elements/shengxin/test/EQA/rawData/202011/Sample202011.R1.fastq.gz";
        InputStream in;
        in = new GZIPInputStream(new FileInputStream(str));

       // IlluminaFastqReader reader = new IlluminaFastqReader();
        SangerFastqReader reader = new SangerFastqReader();
        Iterable<Fastq> fastqs = reader.read(in);
        in.close();
        for(Fastq q : fastqs){
            System.out.println(q.getDescription());
            System.out.println(q.getSequence());
        }
    }
}

package com.heaven7.java.machine.learning.test;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import java.io.*;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 DNA就是脱氧核糖核酸（英语：Deoxyribonucleic acid，缩写为DNA）由含氮的碱基+脱氧核糖+磷酸组成。
 因为核糖和磷酸都一样而碱基又可以分为四种（腺嘌呤A，鸟嘌呤G，胸腺嘧啶T，胞嘧啶C），
 所以脱氧核糖核苷酸就可以分为四种（按照碱基的不同来分）同时在书写过程中可以用这碱基的简写代替。

 RNA就是核糖核酸（RiboNucleic Acid ）由含氮的碱基+核糖+磷酸组成，而组成脱氧核糖核酸的碱基是：腺嘌呤A，鸟嘌呤G，尿嘧啶U，胞嘧啶C。
 同样，也按照碱基的不同把脱氧核糖核酸分为四种，在书写过程中也可以由着四种碱基的简写代替。

 蛋白质就是蛋白质了，由氨基酸构成的生物大分子。

 基因：是遗传的基本单元，是产生一条多肽链或功能RNA所必需的DNA片段。可以简单理解为好长一段DNA链中比较特殊某段。

 染色体:细胞内具有遗传性质的遗传物质深度压缩形成的聚合体，易被碱性染料染成深色，所以叫染色体。

 染色体是由DNA和蛋白质构成的
 */
//DNA: 就是脱氧核糖核酸 (Deoxyribonucleic acid)
//RNA: 核糖核酸（RiboNucleic Acid ）
//protein（蛋白质）： 氨基酸构成的生物大分子
//DNA-> RNA（转录） -> protein （翻译）
public class FastaReaderTest {


    /** Download a large file, e.g. ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_trembl.fasta.gz
     * and pass in path to local location of file
     */
    public static void main(String[] args) {

        String dnaFastaS = ">ENSMUSG00000020122|ENSMUST00000138518\n" +
                "CCCTCCTATCATGCTGTCAGTGTATCTCTAAATAGCACTCTCAACCCCCGTGAACTTGGT\n" +
                "TATTAAAAACATGCCCAAAGTCTGGGAGCCAGGGCTGCAGGGAAATACCACAGCCTCAGT\n" +
                "TCATCAAAACAGTTCATTGCCCAAAATGTTCTCAGCTGCAGCTTTCATGAGGTAACTCCA\n" +
                "GGGCCCACCTGTTCTCTGGT\n" +
                ">ENSMUSG00000020122|ENSMUST00000125984\n" +
                "GAGTCAGGTTGAAGCTGCCCTGAACACTACAGAGAAGAGAGGCCTTGGTGTCCTGTTGTC\n" +
                "TCCAGAACCCCAATATGTCTTGTGAAGGGCACACAACCCCTCAAAGGGGTGTCACTTCTT\n" +
                "CTGATCACTTTTGTTACTGTTTACTAACTGATCCTATGAATCACTGTGTCTTCTCAGAGG\n" +
                "CCGTGAACCACGTCTGCAAT";
        try {

            // automatically uncompresses files using InputStreamProvider
           // InputStreamProvider isp = new InputStreamProvider();

            InputStream inStream = new ByteArrayInputStream(dnaFastaS.getBytes());

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
                    inStream,
                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

            LinkedHashMap<String, ProteinSequence> b;
            int nrSeq = 0;

            while ((b = fastaReader.process(10)) != null) {
                for (String key : b.keySet()) {
                    nrSeq++;
                    System.out.println(nrSeq + " : " + key + " " + b.get(key));
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(FastaReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        URL uniprotFasta = new URL(String.format("https://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("id : %s %s%s%s", uniProtId, seq, System.getProperty("line.separator"), seq.getOriginalHeader());
        System.out.println();

        return seq;
    }
}

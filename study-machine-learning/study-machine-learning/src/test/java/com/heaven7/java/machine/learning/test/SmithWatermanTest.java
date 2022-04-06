package com.heaven7.java.machine.learning.test;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.PairwiseSequenceAligner;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SequencePair;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import java.net.URL;

//Smith Waterman - Local Alignment
public class SmithWatermanTest {

    //DNASequence, RNASequence, ProteinSequence
    public static void main(String[] args) throws Exception {

        String uniprotID1 = "P69905";
        String uniprotID2 = "P68871";

        ProteinSequence s1 = getSequenceForId(uniprotID1);
        ProteinSequence s2 = getSequenceForId(uniprotID2);

        SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getBlosum65();

        GapPenalty penalty = new SimpleGapPenalty();

        int gop = 8;
        int extend = 1;
        penalty.setOpenPenalty(gop);
        penalty.setExtensionPenalty(extend);


        PairwiseSequenceAligner<ProteinSequence, AminoAcidCompound> smithWaterman =
                Alignments.getPairwiseAligner(s1, s2, Alignments.PairwiseSequenceAlignerType.LOCAL, penalty, matrix);

        SequencePair<ProteinSequence, AminoAcidCompound> pair = smithWaterman.getPair();


        System.out.println(pair.toString(60));
    }

    /**
     id : P69905 MVLSPADKTNVKAAWGKVGAHAGEYGAEALERMFLSFPTTKTYFPHFDLSHGSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKLRVDPVNFKLLSHCLLVTLAAHLPAEFTPAVHASLDKFLASVSTVLTSKYR
     sp|P69905|HBA_HUMAN Hemoglobin subunit alpha OS=Homo sapiens OX=9606 GN=HBA1 PE=1 SV=2
     id : P68871 MVHLTPEEKSAVTALWGKVNVDEVGGEALGRLLVVYPWTQRFFESFGDLSTPDAVMGNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPENFRLLGNVLVCVLAHHFGKEFTPPVQAAYQKVVAGVANALAHKYH
     sp|P68871|HBB_HUMAN Hemoglobin subunit beta OS=Homo sapiens OX=9606 GN=HBB PE=1 SV=2
     1                                                         60
 P69905    1 MV-LSPADKTNVKAAWGKVGAHAGEYGAEALERMFLSFPTTKTYFPHF-DLS-----HGS  53
             || |.| .|. | | ||||  .  | | ||| |. . .| |. .|  | |||      |.
 P68871    1 MVHLTPEEKSAVTALWGKV--NVDEVGGEALGRLLVVYPWTQRFFESFGDLSTPDAVMGN  58

     61                                                       120
 P69905   54 AQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKLRVDPVNFKLLSHCLLVTLAAH 113
              .|| |||||  | .. .||.|..    . ||.||  || ||| ||.|| . |.  || |
 P68871   59 PKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKLHVDPENFRLLGNVLVCVLAHH 118

     121                      148
 P69905  114 LPAEFTPAVHASLDKFLASVSTVLTSKY 141
                |||| |.|.  | .| |.  |  ||
 P68871  119 FGKEFTPPVQAAYQKVVAGVANALAHKY 146
     */

    private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        URL uniprotFasta = new URL(String.format("https://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("id : %s %s%s%s", uniProtId, seq, System.getProperty("line.separator"), seq.getOriginalHeader());
        System.out.println();

        return seq;
    }
}

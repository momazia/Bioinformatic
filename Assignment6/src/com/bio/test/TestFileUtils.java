package com.bio.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.pojo.Sequence;
import com.bio.util.FileUtils;

public class TestFileUtils {

	private static final String SEQ_0_HEADER = ">gi|745997998|sp|P0DKH9.1|AREP1_ARATH RecName: Full=Auxin-responsive endogenous peptide 1 [Arabidopsis thaliana]";
	private static final Object SEQ_6_HEADER = ">gi|728048777|sp|I1JLC8.1|SLE2_SOYBN RecName: Full=Protein SLE2; AltName: Full=Em protein; AltName: Full=Protein Glycine max physiologically mature 11; AltName: Full=Protein GmD-19; AltName: Full=Soybean group-1 late embryogenesis abundant protein 2; AltName: Full=Soybean group-1 lea protein 2; Short=Sle2 [Glycine max]";
	private static final String SEQ_6 = "MASRQNNKQELDERARQGETVVPGGTGGKSLEAQQHLAEGRSKGGQTRKEQLGTEGYQEMGRKGGLSTVEKSGEERAQEEGIGIDESKFRTGNNKNQNQNEDQDK";
	public static final String SEQ_0 = "MGLSDCLIYRLVVRCFLDYSICAPFYFYHKFMLSASEPVF";
	public static final String SEQ_1 = "AQCGAQGGGATCPGGLCCSQWGWCGSTPKYCGAGCQSNCR";
	private static final String QUERY_NAME = ">gi|1684788|gb|AAB36530.1| 4-phosphoerythronate dehydrogenase [Escherichia coli str. K-12 substr. W3110]";
	public static final String QUERY_STR = "MKILVDENMPYARDLFSRLGEVTAVPGRPIPVAQLADADALMVRSVTKVNESLLAGKPIKFVGTATAGTDHVDEAWLKQAGIGFSAAPGCNAIAVVEYVFSSLLMLAERDGFSLYDRTVGIVGVGNVGRRLQARLEALGIKTLLCDPPRADRGDEGDFRSLDELVQRADILTFHTPLFKDGPYKTLHLADEKLIRSLKPGAILINACRGAVVDNTALLTCLNEGQKLSVVLDVWEGEPELNVELLKKVDIGTSHIAGYTLEGKARGTTQVFEAYSKFIGHEQHVALDTLLPAPEFGRITLHGPLDQPTLKRLVHLVYDVRRDDAPLRKVAGIPGEFDKLRKNYLERREWSSLYVICDDASAASLLCKLGFNAVHHPAR";

	@Test
	public void testReadQuery() throws IOException {
		Sequence query = FileUtils.getInstance().readQuery(FileUtils.E_COLI_QUERY1_FA);
		assertEquals(QUERY_STR, query.getStr());
		assertEquals(QUERY_NAME, query.getName());
	}

	@Test
	public void testReadSeq() throws IOException {
		List<Sequence> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
		assertEquals(100, seqs.size());
		assertEquals(SEQ_0_HEADER, seqs.get(0).getName());
		assertEquals(SEQ_0, seqs.get(0).getStr());
		assertEquals(SEQ_6, seqs.get(6).getStr());
		assertEquals(SEQ_6_HEADER, seqs.get(6).getName());
	}

}

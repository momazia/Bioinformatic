package com.bio.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.util.FileUtils;

public class TestFileUtils {

	private static final String SEQ_6 = "MASRQNNKQELDERARQGETVVPGGTGGKSLEAQQHLAEGRSKGGQTRKEQLGTEGYQEMGRKGGLSTVEKSGEERAQEEGIGIDESKFRTGNNKNQNQNEDQDK";
	public static final String SEQ_0 = "MGLSDCLIYRLVVRCFLDYSICAPFYFYHKFMLSASEPVF";
	public static final String SEQ_1 = "AQCGAQGGGATCPGGLCCSQWGWCGSTPKYCGAGCQSNCR";
	public static final String DB_STR = "MKILVDENMPYARDLFSRLGEVTAVPGRPIPVAQLADADALMVRSVTKVNESLLAGKPIKFVGTATAGTDHVDEAWLKQAGIGFSAAPGCNAIAVVEYVFSSLLMLAERDGFSLYDRTVGIVGVGNVGRRLQARLEALGIKTLLCDPPRADRGDEGDFRSLDELVQRADILTFHTPLFKDGPYKTLHLADEKLIRSLKPGAILINACRGAVVDNTALLTCLNEGQKLSVVLDVWEGEPELNVELLKKVDIGTSHIAGYTLEGKARGTTQVFEAYSKFIGHEQHVALDTLLPAPEFGRITLHGPLDQPTLKRLVHLVYDVRRDDAPLRKVAGIPGEFDKLRKNYLERREWSSLYVICDDASAASLLCKLGFNAVHHPAR";

	@Test
	public void testReadDb() throws IOException {
		String db = FileUtils.getInstance().readDb(FileUtils.E_COLI_QUERY1_FA);
		assertEquals(DB_STR, db);
	}

	@Test
	public void testReadSeq() throws IOException {
		List<String> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
		assertEquals(100, seqs.size());
		assertEquals(SEQ_0, seqs.get(0));
		assertEquals(SEQ_6, seqs.get(6));
	}

}

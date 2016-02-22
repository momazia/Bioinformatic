import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.main.BranchAndBound;
import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.Sequence;

public class TestProcess {

	@Test
	public void testConstructLeafNodes() {
		List<String> constructLeafNodes = BranchAndBound.getInstance().constructLeafNodes(2);
		// Checking the size
		assertEquals(16, constructLeafNodes.size());
		// Checking randomly
		assertEquals("TT", constructLeafNodes.get(0));
		assertEquals("CT", constructLeafNodes.get(4));
		assertEquals("AA", constructLeafNodes.get(15));
	}

	@Test
	public void testReadSequences() throws IOException {
		List<Sequence> sequences = FileProcessor.getInstance().readSequences("HMP-617.fa");
		// Checking the size
		assertEquals(617, sequences.size());
		// Checking randomly
		assertEquals(">ECSE_0294;coding;x	HMP.18057.AP009240.325077.325307.+", sequences.get(616).getHeader());
		assertTrue(sequences.get(616).getStr().startsWith("ATGCCTGAACTCACTGAACTACCAGAGGGACCGTTTTCGCGCCAG"));
		assertEquals(231, sequences.get(616).getStr().length());
	}

}

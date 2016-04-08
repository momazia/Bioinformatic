package com.bio.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Sequence;
import com.bio.util.FileUtils;
import com.bio.util.SmithWaterman;

public class MainApp {

	public static void main(String[] args) {
		try {

			String db = FileUtils.getInstance().readDb(FileUtils.E_COLI_QUERY1_FA);
			List<Sequence> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
			FileUtils.getInstance().deleteIfExists(FileUtils.OUTPUT_TXT);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FileUtils.IO_PATH + FileUtils.OUTPUT_TXT, true)));
			for (Sequence sequence : seqs) {
				AffineResult affineResult = SmithWaterman.getInstance().run(sequence.getStr(), db);
				SmithWaterman.getInstance().backTrace(sequence.getStr(), db, affineResult);
				FileUtils.getInstance().write(out, affineResult, sequence.getName(), sequence.getStr().length());
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

package com.bio.main;

import java.io.IOException;

import com.bio.main.pojo.MicrobiomeDB;
import com.bio.main.pojo.Query;
import com.bio.main.util.FileUtil;
import com.bio.main.util.MicrobiomeUtil;

public class NRMicrobiomeApp {

	private static final String BLASTN_OUT_95 = "blastn-out-95";
	private static final String METAHIT_NR_HMP_FA = "MetaHIT-nr-HMP.fa";

	public static void main(String[] args) {
		try {
			MicrobiomeDB db = FileUtil.getInstance().readQueries(BLASTN_OUT_95);
			FileUtil.getInstance().saveHeaderFile(METAHIT_NR_HMP_FA, db.getHeader());
			for (Query query : db.getQueries()) {
				MicrobiomeUtil.getInstance().findQueryLength(query);
				MicrobiomeUtil.getInstance().findFindAlignments(query);
				if (!MicrobiomeUtil.getInstance().isQueryEligible(query)) {
					FileUtil.getInstance().saveQuery(METAHIT_NR_HMP_FA, query);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

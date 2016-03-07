package com.bio.main;

import java.io.IOException;

import com.bio.main.pojo.Database;
import com.bio.main.util.FileUtil;
import com.bio.main.util.DatabaseUtil;

public class NRDatabaseApp {

	private static final String META_HIT_NR_HMP_FA = "MetaHIT-nr-HMP.fa";
	private static final String Q_METAHIT_DB_HMP_OUTPUT = "Q-MetaHIT-DB-HMP-output";
	private static final String METAHIT_20000_FA = "MetaHIT-20000.fa";

	public static void main(String[] args) {
		try {
			Database db = FileUtil.getInstance().readBlastNRecords(Q_METAHIT_DB_HMP_OUTPUT);
			DatabaseUtil.getInstance().findDuplicateRecords(db);
			FileUtil.getInstance().copyFileExcludeRedundantQueries(META_HIT_NR_HMP_FA, METAHIT_20000_FA, db.getDuplicateQueries());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.bio.main;

import java.io.IOException;

import com.bio.main.pojo.Database;
import com.bio.main.util.FileUtil;
import com.bio.main.util.DatabaseUtil;

public class NRDatabaseApp {

	public static final String HMP_2000_FA = "HMP-2000.fa";
	public static final String HMP_NR_META_HIT_NR_HMP_FA = "HMP-nr-(MetaHIT-nr-HMP).fa";
	public static final String Q_HMP_DB_META_HIT_NR_HMP_OUTPUT = "Output-Q-HMP-DB-MetaHIT-nr-HMP";
	public static final String META_HIT_NR_HMP_FA = "MetaHIT-nr-HMP.fa";
	public static final String Q_METAHIT_DB_HMP_OUTPUT = "Output-Q-MetaHIT-DB-HMP";
	public static final String METAHIT_20000_FA = "MetaHIT-20000.fa";

	public static void main(String[] args) {
		try {
//			Database db = FileUtil.getInstance().readBlastNRecords(Q_METAHIT_DB_HMP_OUTPUT);
//			DatabaseUtil.getInstance().findDuplicateRecords(db);
//			FileUtil.getInstance().copyFileExcludeRedundantQueries(META_HIT_NR_HMP_FA, METAHIT_20000_FA, db.getDuplicateQueries());
			Database db = FileUtil.getInstance().readBlastNRecords(Q_HMP_DB_META_HIT_NR_HMP_OUTPUT);
			DatabaseUtil.getInstance().findDuplicateRecords(db);
			FileUtil.getInstance().copyFileExcludeRedundantQueries(HMP_NR_META_HIT_NR_HMP_FA, HMP_2000_FA, db.getDuplicateQueries());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

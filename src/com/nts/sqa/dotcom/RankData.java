package com.nts.sqa.dotcom;

import java.util.Comparator;

public class RankData implements Comparable<RankData>{
	private String id = null;
	private String time = null;
	private String misscount = null;
	private String total = null;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public RankData(String id, String time, String misscount, String total) {
		this.id = id;
		this.time =time;
		this.setMisscount(misscount);
		this.total=total;
	}

	@Override
	public int compareTo(RankData d) {
		// TODO Auto-generated method stub
		return this.total.compareTo(d.total);
	}

	public String getMisscount() {
		return misscount;
	}

	public void setMisscount(String misscount) {
		this.misscount = misscount;
	}


}

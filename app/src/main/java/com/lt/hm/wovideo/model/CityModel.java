package com.lt.hm.wovideo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityModel extends MultiItemEntity {
	public static final int TITLE = 1;
	public static final int CITY_LIST = 2;


	private String head;

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	private City city;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public class City {


		/**
		 * city : 澳门
		 * code : 00853
		 * pr : 澳门
		 * prPyAll : aomen
		 * prPyHead : am
		 * prPyOne : a
		 * pyAll : aomen
		 * pyHead : am
		 * pyOne : a
		 */

		private String city;
		private String code;
		private String pr;
		private String prPyAll;
		private String prPyHead;
		private String prPyOne;
		private String pyAll;
		private String pyHead;
		private String pyOne;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getPr() {
			return pr;
		}

		public void setPr(String pr) {
			this.pr = pr;
		}

		public String getPrPyAll() {
			return prPyAll;
		}

		public void setPrPyAll(String prPyAll) {
			this.prPyAll = prPyAll;
		}

		public String getPrPyHead() {
			return prPyHead;
		}

		public void setPrPyHead(String prPyHead) {
			this.prPyHead = prPyHead;
		}

		public String getPrPyOne() {
			return prPyOne;
		}

		public void setPrPyOne(String prPyOne) {
			this.prPyOne = prPyOne;
		}

		public String getPyAll() {
			return pyAll;
		}

		public void setPyAll(String pyAll) {
			this.pyAll = pyAll;
		}

		public String getPyHead() {
			return pyHead;
		}

		public void setPyHead(String pyHead) {
			this.pyHead = pyHead;
		}

		public String getPyOne() {
			return pyOne;
		}

		public void setPyOne(String pyOne) {
			this.pyOne = pyOne;
		}
	}
}

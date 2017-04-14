/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.exapply.app;

/**
 * @author MohammedElshwehy
 */
public final class Constants {

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {

		public static final String ABOUT_US_URL="http://195.39.163.224:25565/clinic/webservices.asmx/AboutUS";
		public static final String TAG_SHARED_PREFS="prefs";
		public static final String TAG_LANGUAGE="language";
		public static final String TAG_TITLE_EN ="TitleEN";
		public static final String TAG_DESCRIPTION_EN = "DescriptionEN";
		public static final String TAG_NEWS_TITLE_EN="NewsTitleEN";
		public static final String TAG_NEWS_DESCRIPTION_EN="NewsContentsEN";
		public static final String TAG_NEWS_IMAGE="Thumbnail";
		public static final String TAG_NEWS_VEDIO_URL="youtubeurl";
		public static final String TAG_OBESITY_CATEGORY_EN="ObesityCategoryNameEN";
		public static final String TAG_RESULT="result";
		public static final String TAG_NEWS_IMAGE_URL="http://clinicalbariatricapp.com/admin/dynamicimages/";
		public static final String TAG_NEWS_YOUTUBE_URL="https://www.youtube.com/watch?v=";
		public static final String TAG_HEIGHT="height";
		public static final String TAG_WEIGHT="weight";
		public static final String TAG_AGE="age";
		public static final String TAG_GENDER="gender";
		public static final String TAG_NEWS_URL="http://clinicalbariatricapp.com/webservices.asmx/News";


	}

	public static int categoryImgs[] = {
			R.drawable.travel_img,
			R.drawable.sport_img,
			R.drawable.charity_img,
			R.drawable.party_img,
			R.drawable.course_img
	};
}

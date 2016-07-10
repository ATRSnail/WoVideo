package com.lt.hm.wovideo.video.model;

import com.google.android.exoplayer.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/8
 */
public class VideoSample {
    public final String name;
    public final String contentId;
    public final String provider;
    public final String uri;
    public final int type;

    public VideoSample(String name, String uri, int type) {
        this(name, name.toLowerCase(Locale.US).replaceAll("\\s", ""), "", uri, type);
    }

    public VideoSample(String name, String contentId, String provider, String uri, int type) {
        this.name = name;
        this.contentId = contentId;
        this.provider = provider;
        this.uri = uri;
        this.type = type;
    }

    public static List<VideoSample> getSamples() {
        List<VideoSample> samples = new ArrayList<>();
        samples.add(new VideoSample("SD",
                "http://111.206.133.42:9900/fifa/video/wovideo/sp1/sp1.m3u8?videoid=xxx&videotype=0&spid=22123&pid=8031006300&preview=1&portalid=369&userid=18510509670&userip=61.148.116.190&spip=111.206.133.39&spport=9910&freetag=1&tradeid=0358c7892acee08f1a0bd5409776c099&lsttm=20160708150648&enkey=5b78eb4cc73eefc5cae7a767dd40cf2e",
                Util.TYPE_HLS));
        samples.add(new VideoSample("HD",
                "http://111.206.133.41:9900/fifa/video/wovideo/hhigh/sp1/sp1.m3u8?videoid=xxx&videotype=0&spid=22123&pid=8031006300&preview=1&portalid=369&userid=18510509670&userip=61.148.116.190&spip=111.206.133.39&spport=9910&freetag=1&tradeid=32e77ef189697c727788a27697e3b182&lsttm=20160708150634&enkey=077f57b1405b9a8de028321e485ad562",
                Util.TYPE_HLS));
        samples.add(new VideoSample("Live",
                "http://111.206.133.53:9910/if5ax/live/live_nsn/index.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&tradeid=130e2360059ad767bcd0b1c8f902b958&lsttm=20160708150424&enkey=cb21d306a9c2904a3421df23b2eec65e",
                Util.TYPE_HLS));

        return samples;
    }
}

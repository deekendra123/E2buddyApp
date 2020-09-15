package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class NoticeData {
    private int noticeId,schoolId,adminId,classId;
    private String noticeTitle, noticeDescription,addedAt,link,noticeType;

    public NoticeData(int noticeId, int schoolId, int adminId, int classId, String noticeTitle, String noticeDescription, String addedAt, String link, String noticeType) {
        this.noticeId = noticeId;
        this.schoolId = schoolId;
        this.adminId = adminId;
        this.classId = classId;
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
        this.addedAt = addedAt;
        this.link = link;
        this.noticeType = noticeType;
    }


    public int getNoticeId() {
        return noticeId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public int getAdminId() {
        return adminId;
    }

    public int getClassId() {
        return classId;
    }


    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeDescription() {
        return noticeDescription;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public String getLink() {
        return link;
    }

    public String getNoticeType() {
        return noticeType;
    }
}

package org.openpaas.paasta.portal.common.api.entity.portal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by indra on 2018-02-07.
 */
@Entity
@Table(name = "buildpack_category")
public class BuildpackCategory {

    //    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "classification", nullable = false)
    private String classification;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "description")
    private String description;

    @Column(name = "buildpack_name", nullable = false)
    private String buildPackName;

    @Column(name = "thumb_img_name")
    private String thumbImgName;

    @Column(name = "thumb_img_path")
    private String thumbImgPath;

    @Column(name = "use_yn", columnDefinition = "default 'Y'", nullable = false)
    private String useYn;

    @Column(name = "app_sample_file_name")
    private String appSampleFileName;

    @Column(name = "app_sample_file_path")
    private String appSampleFilePath;

    @Column(name = "doc_file_url")
    private String docFileUrl;

    @Column(name = "app_sample_file_size")
    private String appSampleFileSize;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "tags_param")
    private String tagsParam;

    @CreationTimestamp
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Column(name = "lastmodified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;

    @Formula("(SELECT cd.value FROM code_detail cd WHERE cd.key = classification AND cd.group_id = 'BUILD_PACK_CATALOG')")
    private String classificationValue;

    @Formula("(SELECT cd.summary FROM code_detail cd WHERE cd.key = classification AND cd.group_id = 'BUILD_PACK_CATALOG')")
    private String classificationSummary;

    @Transient
    private String searchKeyword;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getThumbImgName() {
        return thumbImgName;
    }

    public void setThumbImgName(String thumbImgName) {
        this.thumbImgName = thumbImgName;
    }

    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getAppSampleFileName() {
        return appSampleFileName;
    }

    public void setAppSampleFileName(String appSampleFileName) {
        this.appSampleFileName = appSampleFileName;
    }

    public String getAppSampleFilePath() {
        return appSampleFilePath;
    }

    public void setAppSampleFilePath(String appSampleFilePath) {
        this.appSampleFilePath = appSampleFilePath;
    }

    public String getAppSampleFileSize() {
        return appSampleFileSize;
    }

    public void setAppSampleFileSize(String appSampleFileSize) {
        this.appSampleFileSize = appSampleFileSize;
    }

    public String getDocFileUrl() {
        return docFileUrl;
    }

    public void setDocFileUrl(String docFileUrl) {
        this.docFileUrl = docFileUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getClassificationValue() {
        return classificationValue;
    }

    public void setClassificationValue(String classificationValue) {
        this.classificationValue = classificationValue;
    }

    public String getClassificationSummary() {
        return classificationSummary;
    }

    public void setClassificationSummary(String classificationSummary) {
        this.classificationSummary = classificationSummary;
    }

    public String getBuildPackName() {
        return buildPackName;
    }

    public void setBuildPackName(String buildPackName) {
        this.buildPackName = buildPackName;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getTagsParam() {
        return tagsParam;
    }

    public void setTagsParam(String tagsParam) {
        this.tagsParam = tagsParam;
    }

    @Override
    public String toString() {
        return "BuildpackCategory{" + "no=" + no + ", name='" + name + '\'' + ", classification='" + classification + '\'' + ", summary='" + summary + '\'' + ", description='" + description + '\'' + ", buildPackName='" + buildPackName + '\'' + ", thumbImgName='" + thumbImgName + '\'' + ", thumbImgPath='" + thumbImgPath + '\'' + ", useYn='" + useYn + '\'' + ", appSampleFileName='" + appSampleFileName + '\'' + ", appSampleFilePath='" + appSampleFilePath + '\'' + ", docFileUrl='" + docFileUrl + '\'' + ", appSampleFileSize='" + appSampleFileSize + '\'' + ", userId='" + userId + '\'' + ", tagsParam='" + tagsParam + '\'' + ", created=" + created + ", lastmodified=" + lastmodified + ", classificationValue='" + classificationValue + '\'' + ", classificationSummary='" + classificationSummary + '\'' + ", searchKeyword='" + searchKeyword + '\'' + '}';
    }
}

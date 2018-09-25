package com.day.loan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * @author apple
 * @date 2018/5/18
 *
 */

public class ProductEntity implements Parcelable {
    public static final int TEXT = 1;
    public static final int IMG = 2;


    /**
     * id : 68
     * url : http://www.sina.com.cn/
     * min_algorithm : 0.020
     * fastest_time : 最快10分钟
     * minimum_amount : 30000
     * maximum_amount : 200000
     * interest_algorithm : 0
     * labels : [{"id":"4","name":"信用卡","font":"#00c0ef","background":"#00c0ef","number":"0","status":"0","created_at":"1970-01-01 16:00:00","updated_at":"1970-01-01 16:00:00","pivot":{"product_id":"68","labels_id":"4"}},{"id":"1","name":"身份证","font":"#fb8189","background":"#13a3ff","number":"0","status":"1","created_at":"2017-07-27 08:30:50","updated_at":"2017-07-28 04:09:15","pivot":{"product_id":"68","labels_id":"1"}},{"id":"2","name":"实名手机","font":"#fb8189","background":"#13a3ff","number":"0","status":"1","created_at":"2017-07-28 04:08:33","updated_at":"2017-07-28 04:08:33","pivot":{"product_id":"68","labels_id":"2"}}]
     * apply : 200.1万
     * p_name : 借云
     * p_logo : http://or2eh71ll.bkt.clouddn.com/152056685116029.png
     * p_desc : 房车保单有其一，额度三万起
     */
    private int itemType;
    public ProductEntity(){

    }

    protected ProductEntity(Parcel in) {
        itemType = in.readInt();
        id = in.readString();
        url = in.readString();
        min_algorithm = in.readString();
        fastest_time = in.readString();
        minimum_amount = in.readString();
        maximum_amount = in.readString();
        interest_algorithm = in.readInt();
        apply = in.readString();
        p_name = in.readString();
        p_logo = in.readString();
        p_desc = in.readString();
        labels = in.createTypedArrayList(LabelsBean.CREATOR);
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private String id;
    private String url;
    private String min_algorithm;
    private String fastest_time;
    private String minimum_amount;
    private String maximum_amount;
    private int interest_algorithm;
    private String apply;
    private String p_name;
    private String p_logo;
    private String p_desc;
    private List<LabelsBean> labels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMin_algorithm() {
        return min_algorithm;
    }

    public void setMin_algorithm(String min_algorithm) {
        this.min_algorithm = min_algorithm;
    }

    public String getFastest_time() {
        return fastest_time;
    }

    public void setFastest_time(String fastest_time) {
        this.fastest_time = fastest_time;
    }

    public String getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(String minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public String getMaximum_amount() {
        return maximum_amount;
    }

    public void setMaximum_amount(String maximum_amount) {
        this.maximum_amount = maximum_amount;
    }

    public int getInterest_algorithm() {
        return interest_algorithm;
    }

    public void setInterest_algorithm(int interest_algorithm) {
        this.interest_algorithm = interest_algorithm;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_logo() {
        return p_logo;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }

    public String getP_desc() {
        return p_desc;
    }

    public void setP_desc(String p_desc) {
        this.p_desc = p_desc;
    }

    public List<LabelsBean> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelsBean> labels) {
        this.labels = labels;
    }

    public int getItemType() {
        return itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemType);
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(min_algorithm);
        dest.writeString(fastest_time);
        dest.writeString(minimum_amount);
        dest.writeString(maximum_amount);
        dest.writeInt(interest_algorithm);
        dest.writeString(apply);
        dest.writeString(p_name);
        dest.writeString(p_logo);
        dest.writeString(p_desc);
        dest.writeTypedList(labels);
    }

    public static class LabelsBean implements Parcelable {
        /**
         * id : 4
         * name : 信用卡
         * font : #00c0ef
         * background : #00c0ef
         * number : 0
         * status : 0
         * created_at : 1970-01-01 16:00:00
         * updated_at : 1970-01-01 16:00:00
         * pivot : {"product_id":"68","labels_id":"4"}
         */

        private String id;
        private String name;
        private String font;
        private String background;
        private String number;
        private String status;
        private String created_at;
        private String updated_at;
        private PivotBean pivot;

        public LabelsBean() {
        }

        protected LabelsBean(Parcel in) {
            id = in.readString();
            name = in.readString();
            font = in.readString();
            background = in.readString();
            number = in.readString();
            status = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            pivot = in.readParcelable(PivotBean.class.getClassLoader());
        }

        public static final Creator<LabelsBean> CREATOR = new Creator<LabelsBean>() {
            @Override
            public LabelsBean createFromParcel(Parcel in) {
                return new LabelsBean(in);
            }

            @Override
            public LabelsBean[] newArray(int size) {
                return new LabelsBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public PivotBean getPivot() {
            return pivot;
        }

        public void setPivot(PivotBean pivot) {
            this.pivot = pivot;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(font);
            dest.writeString(background);
            dest.writeString(number);
            dest.writeString(status);
            dest.writeString(created_at);
            dest.writeString(updated_at);
            dest.writeParcelable(pivot, flags);
        }

        public static class PivotBean implements Parcelable{
            /**
             * product_id : 68
             * labels_id : 4
             */

            private String product_id;
            private String labels_id;

            protected PivotBean(Parcel in) {
                product_id = in.readString();
                labels_id = in.readString();
            }

            public PivotBean() {
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(product_id);
                dest.writeString(labels_id);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<PivotBean> CREATOR = new Creator<PivotBean>() {
                @Override
                public PivotBean createFromParcel(Parcel in) {
                    return new PivotBean(in);
                }

                @Override
                public PivotBean[] newArray(int size) {
                    return new PivotBean[size];
                }
            };

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getLabels_id() {
                return labels_id;
            }

            public void setLabels_id(String labels_id) {
                this.labels_id = labels_id;
            }
        }
    }
}

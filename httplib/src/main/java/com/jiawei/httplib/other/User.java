package com.jiawei.httplib.other;

import java.util.List;

/**
 * Created by jiawei on 2017/6/19.
 */

public class User {

    /**
     * data : {"blog":"http://blog.yanzhenjie.com","name":"严振杰","projectList":[{"comment":"Anroid 客户端标准协议网络框架。","id":0,"name":"NoHttp","url":"https://github.com/yanzhenjie/NoHttp"},{"comment":"RecyclerView侧滑菜单、侧滑删除、长按拖拽Item。","id":1,"name":"SwipeRecyclerView","url":"https://github.com/yanzhenjie/SwipeRecyclerView"},{"comment":"Android搭建局域网服务器网络框架。","id":2,"name":"AndServer","url":"https://github.com/yanzhenjie/AndServer"},{"comment":"Anroid6.0运行时权限管理框架。","id":3,"name":"AndPermission","url":"https://github.com/yanzhenjie/AndPermission"}],"url":"http://www.yanzhenjie.com"}
     * error : 1
     */

    private DataBean data;
    private int error;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * blog : http://blog.yanzhenjie.com
         * name : 严振杰
         * projectList : [{"comment":"Anroid 客户端标准协议网络框架。","id":0,"name":"NoHttp","url":"https://github.com/yanzhenjie/NoHttp"},{"comment":"RecyclerView侧滑菜单、侧滑删除、长按拖拽Item。","id":1,"name":"SwipeRecyclerView","url":"https://github.com/yanzhenjie/SwipeRecyclerView"},{"comment":"Android搭建局域网服务器网络框架。","id":2,"name":"AndServer","url":"https://github.com/yanzhenjie/AndServer"},{"comment":"Anroid6.0运行时权限管理框架。","id":3,"name":"AndPermission","url":"https://github.com/yanzhenjie/AndPermission"}]
         * url : http://www.yanzhenjie.com
         */

        private String blog;
        private String name;
        private String url;
        private List<ProjectListBean> projectList;

        public String getBlog() {
            return blog;
        }

        public void setBlog(String blog) {
            this.blog = blog;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<ProjectListBean> getProjectList() {
            return projectList;
        }

        public void setProjectList(List<ProjectListBean> projectList) {
            this.projectList = projectList;
        }

        public static class ProjectListBean {
            /**
             * comment : Anroid 客户端标准协议网络框架。
             * id : 0
             * name : NoHttp
             * url : https://github.com/yanzhenjie/NoHttp
             */

            private String comment;
            private int id;
            private String name;
            private String url;

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "data=" + data +
                ", error=" + error +
                '}';
    }
}

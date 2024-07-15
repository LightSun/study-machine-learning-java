package com.heaven7.java.cmake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmakeWriter {

     private String cmake_version = "3.5";
     private String project_name = "gatk";
     private String project_language = "C CXX";
     private int cxx_stand = 11;

     private String cxx_flags = "-g -O0";
     private String c_flags = "-g -O0";
     private String common_flags = null;

     private List<String> cppPkgs;
     private List<String> messages;

     private List<String> includes;
     private List<String> links;

     private Map<String, List<String>> target_includes;
     private Map<String, List<String>> target_links;
     private Map<String, String> set_vars;
     private boolean generate_source_group;

     private List<ActionParam> actions;

//     static class MsgParam{
//          String fmt;
//          List<String> args;
//     }
     static class FileParam{
          boolean recurse; //recurse or not
          String var;
          List<String> args; // eg: "*.h *.hpp"
     }
     enum ActionType{
          EXECUTABLE, STATIC, SHARED
     }
     static class ActionParam{
          ActionType type;
          String name;
          List<String> vars;  //indirect files by var name
          List<String> files; //direct file
     }
}

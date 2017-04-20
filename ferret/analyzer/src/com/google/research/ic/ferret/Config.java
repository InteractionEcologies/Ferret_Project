/*******************************************************************************
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.google.research.ic.ferret;

import com.google.research.ic.ferret.comm.DeviceEventReceiver;
import com.google.research.ic.ferret.data.DemoManager;
import com.google.research.ic.ferret.data.LogLoader;
import com.google.research.ic.ferret.data.SearchEngine;
import com.google.research.ic.ferret.data.Snippet;
import com.google.research.ic.ferret.data.attributes.AttributeManager;
import com.google.research.ic.ferret.data.attributes.DurationAttributeHandler;
import com.google.research.ic.ferret.data.attributes.UserNameAttributeHandler;
import com.google.research.ic.ferret.test.Debug;
import com.google.research.ic.ferret.uiserver.UIServer;

import java.util.List;

/**
 * TODO: Insert description here. (generated by liyang)
 */
public class Config {
  
  // COMMAND LINE ARGUMENTS
  
  // Runtime configuration arguments
  public static final String ARG_DEBUG = "--debug";  
  public static final String ARG_LOGDIR = "--logdir";
  public static final String ARG_LOGTYPE = "--logtype=";
  public static final String ARG_LOGTYPE_ACCESSIBILITY = "accessibility";
  public static final String ARG_AGGFILTER = "--useAggressiveFiltering";
  public static final String ARG_NGRAMSIZE = "--nGramSize";
  public static final String ARG_NGRAMDENSITY = "--nGramDensity";
  public static final String ARG_ADMITTANCETHRESHHOLD = "--admittanceThreshold";
  public static final String ARG_ELONGATIONFACTOR = "--elongationFactor";
  public static final String ARG_FRACTIONTOMATCH = "--fractionToMatch";

  // Analyzer arguments
  public static final String ARG_LOADDEMOS = "--loaddemos";
  public static final String ARG_DONTLOAD = "--dontload";
  public static final String ARG_DONTINDEX = "--dontindex";
  public static final String ARG_NODEVICESERVER = "--nodeviceserver";
  public static final String ARG_NOUISERVER = "--nouiserver";
  
  // Eval Framework arguments
  public static final String ARG_TAGLIST = "--tags";
  public static final String ARG_QUERYDIR = "--querydir";

  // DEFAULTS
  
  // General defaults (boolean defaults are set at param init)
  public static final String DEFAULT_LOG_DIR = "logs";
  public static final String DEFAULT_LOG_TYPE = ARG_LOGTYPE_ACCESSIBILITY;
  public static final String DEFAULT_DEMO_DIR = "demos";
  
  // Search Algorithm defaults
  public static final int DEFAULT_NGRAM_SIZE = 3;
  public static final double DEFAULT_NGRAM_DENSITY = 0.25;
  public static final double DEFAULT_ADMITTANCE_THRESHHOLD = 0.5;
  public static final int DEFAULT_ELONGATION_FACTOR = 6;
  public static final double DEFAULT_FRACTION_TO_MATCH = 0.25;
  
  // PARAMETERS

  // General
  public static boolean debug = true;
  public static String logDir = DEFAULT_LOG_DIR;
  public static String logType = DEFAULT_LOG_TYPE;

  // Analyzer params
  public static boolean startDeviceServer = true;
  public static boolean startUIServer = true;
  public static boolean loadLogs = true;
  public static boolean indexLogs = true;
  public static boolean loadDemos = false;
 
  // Eval Framework params
  public static String queryDir = null; // if unspecified, EvalFramework will do crossvalidation
  public static String tagList = null; // if unspecified, EvalFramework will use all tags
  
  // Search algo params
    
  /* size of ngrams to use for initial candidate region identification */
  public static int nGramSize = DEFAULT_NGRAM_SIZE;
  
  /* how many ngrams must be found for a region to be considered a candidate? */
  public static double nGramDensity = DEFAULT_NGRAM_DENSITY; 
  
  /* the weighted distance (distance/query.size()) required for admittance to result set */
  public static double admittanceThreshold = DEFAULT_ADMITTANCE_THRESHHOLD; 
  
  /* multiples of query length consider when finding elongations */
  public static int elongationFactor = DEFAULT_ELONGATION_FACTOR;
  
  /* fraction of the query to match at the beginning when finding alternate endings */
  public static double fractionToMatch = DEFAULT_FRACTION_TO_MATCH; 
  
  /* which filtering strategy to use */
  public static boolean useAggressiveFiltering = false;

  
  public static void parseArgs(String[] args) {
    for (String s : args) {
      if (s.equals(Config.ARG_DEBUG)) {
        Config.debug = true;
      } else if (s.startsWith(ARG_LOGDIR)) {
        logDir = s.split("=")[1];
      } else if (s.startsWith(ARG_LOGTYPE)) {
        logType = s.split("=")[1];
        Debug.log("Setting log type to " + logType);
        if (!logType.equalsIgnoreCase(ARG_LOGTYPE_ACCESSIBILITY)) {
          throw new IllegalArgumentException("Log type " + logType + " is not supported.");
        }
      } else if (s.equals(ARG_NODEVICESERVER)) {
        startDeviceServer = false;
      } else if (s.equals(ARG_NOUISERVER)) {
        startUIServer = false;
      } else if (s.equals(ARG_DONTLOAD)) {
        loadLogs = false;
      } else if (s.equals(ARG_DONTINDEX)) {
        indexLogs = false;
      } else if (s.equals(ARG_LOADDEMOS)) {
        loadDemos = true;
      } else if (s.equals(ARG_AGGFILTER)) {
        useAggressiveFiltering = true;
      } else if (s.startsWith(ARG_NGRAMSIZE)) {
        int i = Integer.parseInt(s.split("=")[1]);
        nGramSize = i;
      } else if (s.startsWith(ARG_NGRAMDENSITY)) {
        double d = Double.parseDouble(s.split("=")[1]);
        nGramDensity = d;
      } else if (s.startsWith(ARG_ADMITTANCETHRESHHOLD)) {
        double d = Double.parseDouble(s.split("=")[1]);
        admittanceThreshold = d;
      } else if (s.startsWith(ARG_ELONGATIONFACTOR)) {
        int i = Integer.parseInt(s.split("=")[1]);
        elongationFactor = i;
      } else if (s.startsWith(ARG_FRACTIONTOMATCH)) {
        double d = Double.parseDouble(s.split("=")[1]);
        fractionToMatch = d;
      } else if (s.startsWith(ARG_TAGLIST)) {
        tagList = s.split("=")[1];
      } else if (s.startsWith(ARG_QUERYDIR)) {
        queryDir = s.split("=")[1];
      } else {
        throw new IllegalArgumentException("Unrecognized option: " + s);
      }      
    }
  }
}
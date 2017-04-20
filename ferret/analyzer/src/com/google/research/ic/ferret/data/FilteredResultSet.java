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
package com.google.research.ic.ferret.data;

import com.google.research.ic.ferret.data.attributes.Attribute;
import com.google.research.ic.ferret.data.attributes.AttributeManager;
import com.google.research.ic.ferret.data.attributes.Bin;
import com.google.research.ic.ferret.test.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Insert description here. (generated by marknewman)
 */
public class FilteredResultSet extends ResultSet { 

  public static final int THATSTOOBIG = 10000;
  
  private double minDist = -1.0;
  private double maxDist = -1.0;
  private int size = -1;
  private int minSubSequenceSize = -1;
  private int maxSubSequenceSize = -1;
  private transient ResultSet masterResultSet = null;
  private FilterSpec filterSpec = null;

  public FilteredResultSet(Snippet query, 
      ResultSet masterResultSet, 
      FilterSpec filterSpec) {
    this.sourceQuery = query;
    this.masterResultSet = masterResultSet;
    this.filterSpec = filterSpec;
  }
  
  /**
   * Inner class to simplify JSONification when only a summary is needed
   */ 
  public static final class FilteredResultSummary {

    private String displayName = null;
    private double minDist = -1.0;
    private double maxDist = -1.0;
    private int size = -1;
    private int minSubSequenceSize = -1;
    private int maxSubSequenceSize = -1;
    Map<String, List<Bin>> attrSummaries = null;
    Map<String, Attribute> attributes = null;
    
    public FilteredResultSummary(FilteredResultSet frSet) {
      this.minDist = frSet.minDist;
      this.maxDist = frSet.maxDist;
      this.size = frSet.size;
      this.minSubSequenceSize = frSet.minSubSequenceSize;
      this.maxSubSequenceSize = frSet.maxSubSequenceSize;
      this.attrSummaries = frSet.attrSummaries;
      this.attributes = frSet.attributes;
    }

    public String getDisplayName() {
      return displayName;
    }
    
    public void setDisplayName(String name) {
      displayName = name;
    }
    
    public double getMinDist() {
      return minDist;
    }

    public void setMinDist(double minDist) {
      this.minDist = minDist;
    }

    public double getMaxDist() {
      return maxDist;
    }

    public void setMaxDist(double maxDist) {
      this.maxDist = maxDist;
    }

    public int getSize() {
      return size;
    }

    public void setSize(int size) {
      this.size = size;
    }

    public int getMinSubSequenceSize() {
      return minSubSequenceSize;
    }

    public void setMinSubSequenceSize(int minSubSequenceSize) {
      this.minSubSequenceSize = minSubSequenceSize;
    }

    public int getMaxSubSequenceSize() {
      return maxSubSequenceSize;
    }

    public void setMaxSubSequenceSize(int maxSubSequenceSize) {
      this.maxSubSequenceSize = maxSubSequenceSize;
    }

    public Map<String, List<Bin>> getAttrSummaries() {
      return attrSummaries;
    }

    public void setAttrSummaries(Map<String, List<Bin>> attrSummaries) {
      this.attrSummaries = attrSummaries;
    }

    public Map<String, Attribute> getAttributes() {
      return attributes;  
    }
    
    public void setAttributes(Map<String, Attribute> attributes) {
      this.attributes = attributes;
    }
  }
  
  public FilteredResultSummary getSummary() {
    return new FilteredResultSummary(this);
  }

  /**
   * @param subS
   */
  public void insertIfAcceptable(SubSequence subS) {
    if (results == null) {
      results = new ArrayList<SubSequence>();
    }
    if (filterSpec.filter(subS)) {
      results.add(subS);
      if (minDist == -1.0 || subS.getDistance() < minDist) {
        minDist = subS.getDistance();
      }      
      if (subS.getDistance() > maxDist) {
        maxDist = subS.getDistance();
      }
      if (minSubSequenceSize == -1 || subS.getLength() < minSubSequenceSize) {
        minSubSequenceSize = subS.getLength();
      }
      if (subS.getLength() > maxSubSequenceSize) {
        maxSubSequenceSize = subS.getLength();
      }
      for(Attribute attr : subS.getSnippet().getAttributes()) {
        attributes.put(attr.getKey(), attr);
      }
    }
//    for (String k : attributes.keySet()) {
//      Debug.log("this result set has attribute " + k + ":" + attributes.get(k) + " which is of type " + attributes.get(k).getType());
//    }
    
    Collections.sort(results);
  }

  /**
   * Called after all insertions have been made based on 
   */
  public void cleanUp() {
    removeOverlaps();
    trimToLimit();
    computeSummaries();
    size = getResults().size();
  }
  
  private void removeOverlaps() {
    trimToSize(THATSTOOBIG); // this will take forever if we consider really large ResultSets
    
    for(SubSequence subS : results) {
      
    }
    
    Collections.sort(results, new Comparator<SubSequence>() {
      
      @Override
      public int compare(SubSequence thus, SubSequence that) {
        return (thus.getStartIndex() - that.getStartIndex());
      }
    });

    List<Set<SubSequence>> overlapSets = new ArrayList<Set<SubSequence>>();

    Set<SubSequence> overlapSet = new HashSet<SubSequence>();
    SubSequence prev = null;
    for(SubSequence subS : results) {
      System.err.println(subS);
      if (subS == null) {
        Debug.log("How can a subsequence be null?");
      }

      // check to see if this one overlaps with the previous one
      if (prev != null) {
        //System.err.print("Comparing " + subS + " & " + prev);
        if(subS.getSnippet() == prev.getSnippet() &&
            (subS.getStartIndex() >= prev.getStartIndex() && 
            subS.getStartIndex() < prev.getEndIndex())) {
          //System.err.println(". They overlap");
          overlapSet.add(subS);
          overlapSet.add(prev); // HashSet will ensure unique
        } else { // no overlap, so start a new set
          //System.err.println(". They don't overlap");
          if (overlapSet.size() > 0) {
            overlapSets.add(overlapSet);
            overlapSet = new HashSet<SubSequence>();
          }
        }
        if (overlapSet.size() > 0) {
          overlapSets.add(overlapSet);
        }
      }      
      prev = subS;
    }


    int removed = 0;
    for (Set<SubSequence> oSet : overlapSets) {
      SubSequence bestRepresentative = null;
      for (SubSequence subS : oSet) {
        if (bestRepresentative == null) {
          bestRepresentative = subS;
        }
        if (subS.getDistance() < bestRepresentative.getDistance()) {
          bestRepresentative = subS;
        } 
        results.remove(subS); // take em all out
        removed++;
      }
      results.add(bestRepresentative); // and put the best one back in
      removed--;
    }
    Debug.log("" + overlapSets.size() + " overlap sets were detected, " 
        + removed + " were removed.");
    Collections.sort(results);
  }
  
  /**
   * For all attributes found in this result set
   * Find all values that the attribute can have
   * And count up the number of snippets that have that value
   */
  private void computeSummaries() {
    if (getResults().size() > 0) {
      attrSummaries = AttributeManager.getManager().computeSummaries(this);
      Debug.log("AttrSummaries in FRS: " + attrSummaries);
    }
  }
  
  public void trimToLimit() {
    if (filterSpec.getLimit() != -1) {
      trimToSize(filterSpec.getLimit());
    }
  }

  private void trimToSize(int targetSize) {
    Collections.sort(results);
    if (results.size() > targetSize) {
      results = results.subList(0, targetSize - 1);
    }    
  }
  
}
# Patient Matching Logic

## Overview
The matching algorithm determines if an incoming patient record matches an existing patient in the system.

## Normalization Rules

### Name
- Convert to lowercase
- Trim whitespace

### Phone
- Remove all non-digit characters
- Store normalized value for comparison

### Email
- Convert to lowercase
- Store normalized value

### Date of Birth
- Store in ISO date format (YYYY-MM-DD)

## Matching Rules

### Scoring System
The algorithm scores matches across 4 fields:
1. **Name** (normalized)
2. **Date of Birth**
3. **Phone** (normalized)
4. **Email** (normalized)

### Decision Logic

```java
if (score >= 3) return AUTO_MATCH;
if (score == 2) return REVIEW;
return NO_MATCH;
```

### Confidence Levels

- **AUTO_MATCH** (score ≥ 3): High confidence, automatic match
- **REVIEW** (score = 2): Medium confidence, requires manual review
- **NO_MATCH** (score < 2): Low confidence, no match

### Strong Identifiers
When both phone and email match, confidence is increased. This combination is considered a strong identifier pair.

## Examples

### Example 1: AUTO_MATCH
**Incoming:**
- name: "John Doe"
- dob: "1990-01-01"
- phone: "0812xxx"
- email: "john@gmail.com"

**Existing:**
- name: "john doe" (normalized matches)
- dob: "1990-01-01" (matches)
- phone: "0812xxx" (normalized matches)
- email: "other@mail.com" (no match)

**Score:** 3 matches → **AUTO_MATCH**

### Example 2: REVIEW
**Incoming:**
- name: "Jane Doe"
- dob: "1992-02-02"
- phone: "0813xxx"
- email: "jane@mail.com"

**Existing:**
- name: "Jane Doe" (matches)
- dob: "1992-02-02" (matches)
- phone: "0819xxx" (no match)
- email: "diff@mail.com" (no match)

**Score:** 2 matches → **REVIEW**


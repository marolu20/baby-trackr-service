# BabyTrackr Service
<p>
  <img src="https://img.shields.io/badge/Status-Active_Development-success?style=for-the-badge"/>
</p>

## Overview
BabyTrackr is an event-driven, time-series tracking system with real-time analytics and observability.
It helps caregivers log, monitor, and analyze baby activities such as feeding, diaper changes, and sleep patterns in a structured, data-driven way.
The system provides visibility into daily, weekly, and monthly behavioral trends to support informed caregiving decisions.

## Problem Statement

Tracking infant routines is often fragmented and mentally demanding.

Caregivers struggle with:
- inconsistent tracking of daily activities
- lack of visibility into behavioral patterns
- cognitive overload from manual logging
- difficulty maintaining structured routines

This can lead to stress, missed events, and difficulty identifying patterns in behavior.
BabyTrackr aims to solve this through structured event tracking and real-time analytics.


## System Architecture
![Image of Flow Chart](images/baby-trackr-sys-design-flowchart.png)
### High Level Design

At a high level, the design of the system can be categorized into 3 separate flows:
Write, Process, and Read.

<b>Write</b></br>
User → Client → API Service → Database + Kafka
- Users log baby events (feeding, sleep, diaper changes)
- API service handles CRUD operations
- Events are persisted and published to Kafka in real time

<b>Process</b></br>
Kafka → Analytics Service → Analytics Database
- Kafka streams events to processing layer
- Analytics service performs aggregations:
    - daily summaries
    - weekly trends
    - monthly patterns
- Results stored in analytics database

<b>Read</b></br>
User → Dashboard → Reporting API → Analytics DB → Response
- Dashboard queries reporting API
- API retrieves aggregated analytics data
- Users view real-time insights and trends
---

## Implementation

### Backend
- Kotlin + Spring Boot REST APIs
- Event-driven architecture with Kafka
-  Follows Clean Architecture principles

### Data Layer
- Operational database for event storage
- Analytics database for aggregated time-series data

### Analytics Dashboard & Frontend
- Real-time analytics dashboard built with React
- Visualization of daily, weekly, and monthly activity trends
- Interactive insights for feeding, sleep, and diaper events
- API-driven data rendering from backend reporting service

### Observability
- Prometheus for metrics
- Grafana for visualization

#### Deployment
- Dockerized services for portability and scalability

---

## Phase Roadmap

### Phase 2: AI-Powered Insights Engine (Planned)
This phase introduces intelligence on top of the event-driven data platform.
- Ask questions about baby activity data using natural language
- Retrieve relevant historical events using a RAG pipeline
- Generate contextual insights using LLMs grounded in real data
- Provide AI-generated summaries alongside existing dashboard analytics

Example:
- “How has my baby been sleeping this week?”
- “When was the last feeding today?”
- “Are there any patterns in nighttime wake-ups?”

### Phase 3: Authentication & Multi-User Support (Planned)

This phase transitions BabyTrackr into a secure multi-user system.
- User authentication and authorization (OAuth2 / JWT)
- Multi-user data isolation

---
## Goal
To build a real-time, event-driven analytics platform that evolves into an AI-assisted caregiving system, combining backend systems, data engineering, and applied AI.
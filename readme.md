# Auto-Complete Suggestions API

## Overview

This is a Spring Boot 3.4.2 REST API that provides auto-complete suggestions for large cities based on a partial search term. It allows optional geolocation-based ranking using latitude and longitude parameters.

## Features

- Exposes an endpoint at `/suggestions`
- Supports partial or complete search queries (`q` parameter)
- Supports optional latitude and longitude parameters to improve ranking
- Returns JSON results sorted by confidence score (0 to 1)

## Example Request

```
GET /suggestions?q=Londo&latitude=43.70011&longitude=-79.4163
```

### Example Response

```json
{
  "suggestions": [
    {
      "name": "London, ON, Canada",
      "latitude": "42.98339",
      "longitude": "-81.23304",
      "score": 0.9
    },
    {
      "name": "London, OH, USA",
      "latitude": "39.88645",
      "longitude": "-83.44825",
      "score": 0.5
    }
  ]
}
```

## Technologies Used

- Java 21
- Spring Boot 3.4.2
- Gradle

## Setup & Installation

### 1. Clone the Repository

```sh
git clone https://github.com/chrissandi/lacakio-suggestion.git
```

### 2. Build the Application

```sh
./gradlew build
```

### 3. Run Locally

```sh
./gradlew bootRun
```

# AOPoller

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

AOPoller is a Java-based application designed for [briefly describe the purpose or functionality of the project]. This project is structured to facilitate easy integration and extension of its core functionalities.

## Features

- Apache Nutch Functions as APIs
- URL Scrapping
- Both with selenium driver and without selenium drivers.
- ...

## Technologies Used

- Java
- Spring Boot
- Docker
- Apache Nutch

## Getting Started

### Prerequisites

Ensure you have the following installed:

- Java 8 or higher
- Maven
- Docker

### Installation

1. Clone the repository:

\`\`\`bash
git clone https://github.com/ahmedmustaq/aopoller.git
cd aopoller
\`\`\`

2. Build the project using Maven:

\`\`\`bash
./mvnw clean install
\`\`\`

3. Build the Docker image:

\`\`\`bash
docker build -t aopoller .
\`\`\`

### Running the Application

Start the application using Docker Compose:

\`\`\`bash
docker-compose up
\`\`\`

The application will be accessible at `http://localhost:8080`.

## Configuration

The configuration files are located in the `src/main/resources` directory. Key configuration files include:

- `application.properties`: Main configuration file for Spring Boot.
- `docker-compose.yml`: Configuration for Docker Compose to orchestrate the services.

## Usage

### Basic Usage

The poller is meant to scrap on ecommerce website for some valuable information without bothering their availability. It helps in promotion of the ecommerce websit
and their products if they don't have product apis. In other words you can convert any website into apis using scraping.

### Custom Extractors

You can define custom extractors in the `custom-extractors.xml` file. Here you can configure the xpath to retreive the html elements to scrap

## Contributing

We welcome contributions! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

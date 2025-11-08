# AI in Java Applications

This project is inspired by the examples in the book *"Understanding LangChain4j"* by Antonio Goncalves.  
I am using it to deepen my understanding of AI integration in applications and software development.  
While much of the project follows the book’s examples, it also includes my own experiments, ideas, and tests.

---

## Prerequisites

Before running the project, you need **Docker Model Runner** enabled.  
Follow the official guide here: [Getting Started with Docker Model Runner](https://docs.docker.com/ai/model-runner/get-started/).

Enabling Docker Model Runner allows you to easily **pull and run AI models** locally using Docker.

---

## Steps to Run the Project

### Step 1: Clone this project

Clone this repository to your local machine.

---

### Step 2: Enable Docker Model Runner

Make sure Docker Model Runner is running on your machine.

```bash
docker model status
```
---

### Step 3: Pull a Model

Pull the AI model you want to use. For example, to pull LLaMA 3:

```bash
docker model pull ai/llama3.2:3B-Q4_K_M
```

make sure the model exists

```bash
docker model list
```

---

### Step 4: Run the application
- BASE_URL should point to your local Docker Model Runner endpoint.
- MODEL should match the model you pulled in Step 3.

```bash
mvn clean compile exec:java -DBASE_URL=http://localhost:12434/engines/llama.cpp/v1 -DMODEL=ai/llama3.2:3B-Q4_K_M
```

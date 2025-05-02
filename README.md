# Smart Powerstrip Controller App

This is a mobile Android application for remotely monitoring and controlling a smart power strip connected to an ESP32 microcontroller. The app lets users:

-  Toggle ON/OFF states of 4 outlets
-  View historical power consumption graphs per outlet
-  Communicate with the ESP32 over RabbitMQ (via a Node.js relay server)
-  Retrieve real-time and historical data from an AWS-hosted MySQL database

---

##  Features

- **Outlet Status Page**  
  Displays toggle switches for 4 outlets. Toggling sends a message to RabbitMQ to control physical relays on the ESP32.

- **Graph Page**  
  Fetches historical power data via a Node.js REST API and visualizes power usage for each outlet using MPAndroidChart.

- **Backend Integration**  
  - RabbitMQ for message brokering (`mqtt-subscription-403144qos0`)
  - Node.js relay server for sending commands
  - AWS EC2 + MySQL RDS for storing and serving power usage data

---

##  Technologies Used

| Tech               | Purpose                        |
|--------------------|-------------------------------|
| Android (Java)     | Mobile app                    |
| MPAndroidChart     | Line charts for power history |
| RabbitMQ           | IoT messaging with ESP32      |
| Node.js            | Backend relay for toggle API  |
| AWS RDS (MySQL)    | Power data storage            |
| AWS EC2            | Host for Node.js server       |
| Volley             | HTTP API communication        |

---

##  Testing Setup

Contact Shaharyaar Samuel for Details regarding testing setup

---

##  Getting Started

### Prerequisites

- Android Studio (latest version)
- Git + GitHub
- A running Node.js relay server
- Access to the RabbitMQ broker and MySQL DB

### Clone and Build

```bash
git clone https://github.com/shaharyaarsamu1/SeniorDesign25App.git
cd SeniorDesign25App

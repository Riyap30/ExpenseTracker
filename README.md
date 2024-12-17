
# **SplitwiseAI**

SplitwiseAI is an AI-driven expense tracking system that enhances traditional expense-tracking applications by incorporating features such as voice-based expense entry and AI-driven automatic categorization. It provides users with group-based tracking, global and group-specific category management, and advanced automation capabilities using OpenAI's Whisper and ChatGPT models.

---

## **Features**
- **Global and Group-Specific Categories**: Manage predefined global categories and create custom group-specific categories for personalized expense tracking.
- **Group and User Management**: Create groups, manage members, and assign roles with specific permissions.
- **Expense Entry and Tracking**: Add expenses with details like amount, description, and category; split payments among group members; and track balances.
- **Voice-Based Expense Entry**: Utilize OpenAI’s Whisper API to convert voice input into text for hands-free expense entry.
- **AI-Powered Category Inference**: Leverage OpenAI's ChatGPT to automatically categorize expenses based on descriptions, reducing manual effort.

---

## **Installation and Setup**

### **Backend**

#### **Prerequisites**
- **JDK 17**
- **Apache Maven**
- **PostgreSQL** (for production)
- **H2 Database** (for testing)

---

### 📦 **Step 1: Database Setup (PostgreSQL)**
This project requires **PostgreSQL** as the primary database for storing user, expense, and group data.

#### 🖥️ **macOS**
1. **Install PostgreSQL** using Homebrew:
   ```bash
   brew install postgresql
   ```

2. **Start the PostgreSQL service**:
   ```bash
   brew services start postgresql
   ```

3. **Verify PostgreSQL is running**:
   ```bash
   brew services list
   ```

4. **Access the PostgreSQL shell**:
   ```bash
   psql -U postgres
   ```

5. **Create the `expensesplit` database**:
   ```sql
   CREATE DATABASE expensesplit;
   ```

6. **Exit the PostgreSQL shell**:
   ```sql
   \q
   ```

---

#### 🖥️ **Windows**
1. **Download and install PostgreSQL** from [https://www.postgresql.org/download/](https://www.postgresql.org/download/).

2. **During the installation**, set up the following:
   - **Username**: `postgres`
   - **Password**: Set your own password (for this guide, we'll assume it's `post@1234`).

3. **Start the PostgreSQL service** from **pgAdmin** or **Windows Services**.

4. **Open the command prompt (cmd) or PowerShell** and access the PostgreSQL shell:
   ```bash
   psql -U postgres
   ```

5. **Create the `expensesplit` database**:
   ```sql
   CREATE DATABASE expensesplit;
   ```

6. **Exit the PostgreSQL shell**:
   ```sql
   \q
   ```

---

#### 🖥️ **Linux (Ubuntu/Debian)**
1. **Update the system**:
   ```bash
   sudo apt update && sudo apt upgrade
   ```

2. **Install PostgreSQL**:
   ```bash
   sudo apt-get install postgresql postgresql-contrib
   ```

3. **Start the PostgreSQL service**:
   ```bash
   sudo systemctl start postgresql
   ```

4. **Switch to the postgres user**:
   ```bash
   sudo -i -u postgres
   ```

5. **Access the PostgreSQL shell**:
   ```bash
   psql
   ```

6. **Create the `expensesplit` database**:
   ```sql
   CREATE DATABASE expensesplit;
   ```

7. **Exit the PostgreSQL shell**:
   ```sql
   \q
   ```

8. **Exit from the postgres user**:
   ```bash
   exit
   ```

---

### 🔐 **Step 2: Update Database and API Credentials**

1. Open the **`src/main/resources/application.properties`** file.

2. Update the **PostgreSQL credentials** as follows:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/expensesplit
   spring.datasource.username=postgres
   spring.datasource.password=post@1234
   spring.datasource.driver-class-name=org.postgresql.Driver

   # Hibernate Configuration
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

3. Update the **OpenAI ChatGPT API Key** configuration:
   ```properties
   openai.api.key=your_openai_api_key
   ```
   > 🔥 **Note:** Replace `your_openai_api_key` with your actual OpenAI API key.

4. **Save the file**.

---

### 🚀 **Run the Application**
1. **Navigate to the project directory**:
   ```bash
   cd /path/to/SplitwiseAI
   ```


2. **Install Dependencies**:
   ```bash
   mvn install
   ```

3. **Run the application** using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

    **Note:** Make the mvnw script executable (only needed if you face a “permission denied” error):
    ```bash
    chmod +x ./mvnw
    ```

4. **Check the application logs** to ensure the database and OpenAI API are connected successfully.

---

### **Frontend Setup**

#### **Prerequisites**
- **Node.js**
- **npm**
- **Vite**
- **TypeScript**

---

### 📦 **Frontend Installation**
1. **Navigate to the Frontend Directory**:
   ```bash
   cd front-end
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Run the Development Server**:
   ```bash
   npm run dev
   ```

4. **Access the application** at the URL provided, typically `http://localhost:5173/`.

---

## **Usage**
1. **Register and Log In**: Create a new account or log in with existing credentials.  
2. **Create Groups**: Set up groups for different expense-sharing scenarios.  
3. **Manage Categories**: Use global categories or create group-specific ones to organize expenses.  
4. **Add Expenses**: Enter expenses manually or use the voice-based feature for hands-free entry.  
5. **AI-Powered Categorization**: Leverage ChatGPT to automatically categorize expenses.  

---

## **Contributing**
Contributions are welcome! Please fork the repository and create a pull request with your changes.



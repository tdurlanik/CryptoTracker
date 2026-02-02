# 📈 CryptoMath

This project is my attempt to build a cryptocurrency tracking app **without using any third-party graphing libraries**. Instead of relying on ready-made solutions like MPAndroidChart, I wanted to apply my mathematical background to build a custom **Graph Engine** from scratch using Android Canvas and linear interpolation.

## 🚀 Why I Built This?
Most apps use libraries to draw charts. I asked myself: *"Can I calculate the pixel coordinates of Bitcoin's price volatility using pure math?"*
The answer is **CryptoMath**.

## 💡 Key Features
* **Custom Math-Based Charts:** I wrote the algorithms to normalize price data and render it on the screen using Bezier curves and paths.
* **Real-Time Data:** Fetches live market data using the **CoinGecko API**.
* **Time Travel:** Users can switch between 7-day, 1-month, and 1-year historical data.
* **Interactive UI:** Shows the current price dynamically on the graph axis.
* **Smart State Management:** Built with **MVVM** and **StateFlow** to handle data efficiently.

## 🛠 Tech Stack
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Modern Android UI)
* **Networking:** Retrofit & Gson
* **Architecture:** MVVM (Model-View-ViewModel)
* **Graphics:** Native Canvas API

## 🧮 The Math Behind It
To draw the graph, I used a normalization formula to map the price range to the screen height:

$$Y_{screen} = Height - \left( \frac{Price - MinPrice}{MaxPrice - MinPrice} \times Height \right)$$

This ensures that whether the price is $0.1 or $60,000, the graph always scales perfectly to the screen.

## 📸 Screenshots

<img width="324" height="720" alt="Screenshot" src="https://github.com/user-attachments/assets/380e8d3c-df66-47ed-a3dc-6dd1904f02e6" />

---
*Built  by [Tayfur Durlanık]*

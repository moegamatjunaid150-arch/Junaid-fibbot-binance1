# Fib Bot - Binance Fibonacci Trading Bot for Android

A professional-grade Android trading application built with Jetpack Compose and Material 3, featuring real-time Fibonacci retracement analysis, technical indicators, and paper trading simulation.

## Features

### 📊 Core Trading Features
- **Real-time Watchlist**: Live spot trading pairs from Binance WebSocket
- **TradingView-style Charts**: Interactive candlestick charts with multiple timeframes
- **Fibonacci Retracement**: Automatic calculation from recent swing highs/lows
- **Technical Indicators**: 9 EMA, 21 EMA, RSI (14), MACD
- **Trading Signals**: Intelligent Buy/Sell/Hold signals based on multi-indicator analysis
- **Paper Trading**: Risk-free simulation mode (default)
- **Trade History**: Complete trade records with profit/loss statistics

### 🔐 Security & Configuration
- **Secure API Key Storage**: Android EncryptedSharedPreferences
- **Risk Management**: Stop-loss, take-profit, max trade size, daily loss limit
- **Settings Screen**: API key and strategy configuration

### 🎨 User Interface
- **Material 3 Design**: Modern, responsive UI
- **Dark Mode Support**: Full dark theme
- **Real-time Notifications**: Signal alerts
- **Loading Indicators**: Smooth UX feedback

## Quick Start

### Prerequisites
- Android Studio 2022.1+
- Android SDK 26+ (API level)
- Kotlin 1.8+

### Setup

1. **Clone Repository**
   ```bash
   git clone https://github.com/moegamatjunaid150-arch/Junaid-fibbot-binance1.git
   ```

2. **Get Binance API Keys**
   - Visit https://www.binance.com
   - Go to Account → API Management
   - Create new API key with Spot Trading permissions
   - Copy API Key and Secret Key

3. **Build & Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

4. **Configure API Keys**
   - Open app
   - Go to Settings
   - Enter API Key and Secret Key (encrypted storage)
   - Save and test connection

## Architecture

- **MVVM Pattern**: Clean separation of concerns
- **Repository Pattern**: Data abstraction
- **Room Database**: Local persistence
- **Retrofit**: REST API
- **OkHttp WebSockets**: Real-time streaming
- **Hilt**: Dependency injection
- **Coroutines & Flow**: Async operations

## Technical Stack

- Jetpack Compose for UI
- Material 3 Design System
- Room for Database
- Retrofit & OkHttp for APIs
- Hilt for DI
- Kotlin Coroutines
- AndroidX Security for encryption

## Project Structure

```
app/src/main/java/com/fibbot/
├── api/              # Binance API clients
├── database/         # Room database & DAOs
├── models/           # Data models
├── repository/       # Data repositories
├── strategy/         # Trading strategy logic
├── ui/              # Compose screens & components
├── viewmodel/       # MVVM ViewModels
└── utils/           # Utilities & constants
```

## Usage

### Main Screen
- View real-time watchlist
- See current trading signals
- Quick access to charts and history

### Watchlist
- Live price updates
- 24h change percentages
- Volume information
- Tap to view detailed chart

### Chart
- Interactive candlestick charts
- Multiple timeframes (1m, 5m, 15m, 1h, 4h, 1d)
- Fibonacci levels overlay
- Technical indicator overlays
- Trading signals

### Trade History
- All trades executed in paper trading
- Entry/exit prices
- Profit/loss per trade
- Win rate statistics
- Performance analytics

### Settings
- API key management
- Trading pair configuration
- Strategy parameter tuning
- Risk management settings
- Test API connection

## Security

✅ Implemented:
- Encrypted API key storage (EncryptedSharedPreferences)
- HTTPS-only API communication
- No API keys in logs
- Secure signature generation

⚠️ Best Practices:
- Enable IP whitelist on Binance
- Use read-only API keys when possible
- Rotate keys periodically
- Monitor account activity

## Troubleshooting

**API Connection Failed**
- Check internet connection
- Verify API credentials in Settings
- Ensure correct Binance API permissions
- Check IP whitelist settings

**WebSocket Issues**
- App auto-reconnects
- Check network connectivity
- Clear app cache
- Restart app

**No Signals Generated**
- Verify candlestick data loading
- Check strategy parameters
- Ensure sufficient historical data (21+ candles)
- Review logs

## Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Future Features

- [ ] Real trading capability
- [ ] Futures trading support
- [ ] Advanced chart tools
- [ ] ML-based signals
- [ ] Multi-exchange support
- [ ] Cloud backup
- [ ] Bot automation
- [ ] Strategy backtesting

## License

MIT License - See LICENSE file

## Support

For issues:
1. Check Troubleshooting section
2. Review GitHub Issues
3. Check Binance API docs
4. Create new GitHub Issue with details

## Disclaimer

**IMPORTANT**: This is a simulation app (paper trading only). Always:
- Test strategies thoroughly
- Understand trading risks
- Never share API keys
- Monitor your account
- Do your own research

**Trading involves substantial risk of loss.**

---

Version 1.0.0 | July 2026
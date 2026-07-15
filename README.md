# FibBot - Fibonacci Retracement Trading Bot

A sophisticated Android trading bot that leverages Fibonacci retracement levels combined with technical indicators (EMA, RSI, MACD, ATR, Bollinger Bands) to generate automated trading signals on the Binance exchange.

## Features

### Core Trading Strategy
- **Fibonacci Retracement Analysis**: Automatic identification of swing highs/lows and calculation of key Fibonacci levels (23.6%, 38.2%, 50%, 61.8%, 78.6%)
- **Multi-Indicator Analysis**: 
  - EMA (9 & 21 period)
  - RSI (14 period)
  - MACD with signal line
  - ATR for volatility measurement
  - Bollinger Bands for range identification

### Signal Generation
- Confidence-based trading signals (BUY/SELL/HOLD)
- Signal strength calculation based on indicator convergence
- Real-time analysis of market conditions

### Risk Management
- Dynamic position sizing based on account risk
- Stop-loss and take-profit calculation
- Daily loss limits
- Trade validation and risk assessment

### Data Management
- Real-time candle data from Binance
- Local Room database for persistent storage
- Price caching system
- Trade history tracking

### User Interface
- **Trading Screen**: Active trade management and portfolio statistics
- **Chart Screen**: Real-time technical analysis visualization
- Material Design 3 with Jetpack Compose
- Dark/Light theme support

## Architecture

```
com.fibbot/
├── api/                 # Binance REST and WebSocket clients
├── database/            # Room database entities and DAOs
├── di/                  # Dependency injection
├── models/              # Data models and DTOs
├── repository/          # Data layer repositories
├── strategy/            # Trading logic and calculations
├── ui/                  # Jetpack Compose screens
└── viewmodel/           # MVVM ViewModels
```

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **Serialization**: Gson, Kotlinx Serialization
- **Logging**: Timber

## Building & Running

```bash
# Clone the repository
git clone <repository-url>

# Build the project
./gradlew build

# Run on emulator/device
./gradlew installDebug
```

## Configuration

Key configuration parameters in `RiskManager`:
- **Account Balance**: $1000 (default)
- **Risk Per Trade**: 2% (default)
- **Max Daily Loss**: 5% (default)

## API Integration

- **REST API**: Binance REST endpoint for historical data and current prices
- **WebSocket**: Real-time price and candle updates
- Base URL: `https://api.binance.com`

## Trading Modes

- **Paper Trading**: Demo mode for testing strategies without real capital
- Configurable for live trading with proper risk management

## Performance

- Efficient multi-indicator calculations
- Local caching to reduce API calls
- Coroutine-based async operations
- Optimized database queries

## Future Enhancements

- Advanced machine learning signal validation
- Portfolio backtesting engine
- Multi-timeframe analysis
- Advanced position management
- Strategy performance analytics
- Webhook notifications

## License

MIT License

## Support

For issues, feature requests, or contributions, please open a GitHub issue or pull request.

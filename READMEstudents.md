com.rentaherramientas.tolly
│
├── TollyApplication.java
│
├── application
│   ├── dto
│   │   ├── auth
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── RefreshTokenRequest.java
│   │   │
│   │   ├── tool
│   │   │   ├── CreateToolRequest.java
│   │   │   ├── UpdateToolRequest.java
│   │   │   └── ToolResponse.java
│   │   │
│   │   ├── reservation
│   │   │   ├── CreateReservationRequest.java
│   │   │   └── ReservationResponse.java
│   │   │
│   │   └── payment
│   │       ├── PaymentRequest.java
│   │       └── InvoiceResponse.java
│   │
│   ├── mapper
│   │   ├── UserMapper.java
│   │   ├── RoleMapper.java
│   │   ├── ToolMapper.java
│   │   ├── ReservationMapper.java
│   │   └── PaymentMapper.java
│   │
│   └── usecase
│       ├── auth
│       │   ├── LoginUseCase.java
│       │   ├── LogoutUseCase.java
│       │   ├── RefreshTokenUseCase.java
│       │   └── ChangePasswordUseCase.java
│       │
│       ├── user
│       │   ├── CreateUserUseCase.java
│       │   ├── GetCurrentUserUseCase.java
│       │   └── AssignRoleUseCase.java
│       │
│       ├── tool
│       │   ├── CreateToolUseCase.java
│       │   ├── UpdateToolUseCase.java
│       │   ├── DeleteToolUseCase.java
│       │   └── ListAvailableToolsUseCase.java
│       │
│       ├── reservation
│       │   ├── CreateReservationUseCase.java
│       │   ├── CancelReservationUseCase.java
│       │   └── GetReservationsByUserUseCase.java
│       │
│       └── payment
│           ├── RegisterPaymentUseCase.java
│           └── GenerateInvoiceUseCase.java
│
├── domain
│   ├── model
│   │   ├── User.java
│   │   ├── Role.java
│   │   ├── Tool.java
│   │   ├── Reservation.java
│   │   ├── Payment.java
│   │   └── enums
│   │       ├── UserStatus.java
│   │       ├── ToolStatus.java
│   │       └── ReservationStatus.java
│   │
│   ├── ports
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   ├── ToolRepository.java
│   │   ├── ReservationRepository.java
│   │   ├── PaymentRepository.java
│   │   ├── TokenService.java
│   │   └── PasswordService.java
│   │
│   └── exceptions
│       ├── DomainException.java
│       ├── UserNotFoundException.java
│       ├── ToolNotFoundException.java
│       ├── ReservationException.java
│       └── PaymentException.java
│
└── infrastructure
    ├── config
    │   ├── OpenApiConfig.java
    │   └── DataInitializer.java
    │
    ├── exception
    │   ├── ErrorResponse.java
    │   └── GlobalExceptionHandler.java
    │
    ├── security
    │   ├── JwtAuthenticationFilter.java
    │   ├── JwtTokenService.java
    │   ├── PasswordServiceImpl.java
    │   └── SecurityConfig.java
    │
    ├── persistence
    │   ├── entity
    │   │   ├── UserEntity.java
    │   │   ├── RoleEntity.java
    │   │   ├── ToolEntity.java
    │   │   ├── ReservationEntity.java
    │   │   └── PaymentEntity.java
    │   │
    │   ├── repository
    │   │   ├── UserJpaRepository.java
    │   │   ├── RoleJpaRepository.java
    │   │   ├── ToolJpaRepository.java
    │   │   ├── ReservationJpaRepository.java
    │   │   └── PaymentJpaRepository.java
    │   │
    │   └── adapters
    │       ├── in
    │       │   └── rest
    │       │       ├── AuthController.java
    │       │       ├── UserController.java
    │       │       ├── ToolController.java
    │       │       ├── ReservationController.java
    │       │       └── PaymentController.java
    │       │
    │       └── out
    │           └── persistence
    │               ├── UserRepositoryAdapter.java
    │               ├── ToolRepositoryAdapter.java
    │               ├── ReservationRepositoryAdapter.java
    │               └── PaymentRepositoryAdapter.java

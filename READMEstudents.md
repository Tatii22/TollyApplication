src
└── main
    ├── java
    │   └── com
    │       └── rentaherramientas
    │           └── tolly
    │               ├── TollyApplication.java
    │
    │               ├── application
    │               │   ├── dto
    │               │   │   ├── auth
    │               │   │   │   ├── LoginRequest.java
    │               │   │   │   ├── LoginResponse.java
    │               │   │   │   ├── RefreshTokenRequest.java
    │               │   │   │   └── RegisterRequest.java
    │               │   │   │
    │               │   │   ├── tool
    │               │   │   │   ├── CreateToolRequest.java
    │               │   │   │   └── ToolResponse.java
    │               │   │   │
    │               │   │   ├── payment
    │               │   │   │   ├── CreatePaymentRequest.java
    │               │   │   │   └── PaymentResponse.java
    │               │   │   │
    │               │   │   ├── reservation
    │               │   │   │   ├── CreateReservationRequest.java
    │               │   │   │   └── ReservationResponse.java
    │               │   │   │
    │               │   │   ├── AssignRoleRequest.java
    │               │   │   ├── ChangePasswordRequest.java
    │               │   │   ├── RoleResponse.java
    │               │   │   └── UserResponse.java
    │               │   │
    │               │   ├── mapper
    │               │   │   ├── RoleMapper.java
    │               │   │   ├── ToolMapper.java
    │               │   │   ├── UserMapper.java
    │               │   │   ├── PaymentMapper.java
    │               │   │   └── ReservationMapper.java
    │               │   │
    │               │   └── usecase
    │               │       ├── auth
    │               │       │   ├── LoginUseCase.java
    │               │       │   ├── LogoutUseCase.java
    │               │       │   ├── RefreshTokenUseCase.java
    │               │       │   └── ChangePasswordUseCase.java
    │               │       │
    │               │       ├── user
    │               │       │   ├── CreateUserUseCase.java
    │               │       │   ├── AssignRoleUseCase.java
    │               │       │   └── GetCurrentUserUseCase.java
    │               │       │
    │               │       ├── tool
    │               │       │   ├── CreateToolUseCase.java
    │               │       │   └── GetToolsUseCase.java
    │               │       │
    │               │       ├── payment
    │               │       │   └── CreatePaymentUseCase.java
    │               │       │
    │               │       └── reservation
    │               │           └── CreateReservationUseCase.java
    │               │
    │               ├── domain
    │               │   ├── exceptions
    │               │   │   ├── DomainException.java
    │               │   │   ├── InvalidCredentialsException.java
    │               │   │   ├── UserNotFoundException.java
    │               │   │   ├── ToolNotFoundException.java
    │               │   │   └── ReservationException.java
    │               │   │
    │               │   ├── model
    │               │   │   ├── enums
    │               │   │   │   ├── ToolStatus.java
    │               │   │   │   ├── ReservationStatus.java
    │               │   │   │   └── PaymentStatus.java
    │               │   │   │
    │               │   │   ├── User.java
    │               │   │   ├── Role.java
    │               │   │   ├── Tool.java
    │               │   │   ├── Reservation.java
    │               │   │   ├── Payment.java
    │               │   │   ├── RefreshToken.java
    │               │   │   └── UserStatus.java
    │               │   │
    │               │   └── ports
    │               │       ├── UserRepository.java
    │               │       ├── RoleRepository.java
    │               │       ├── ToolRepository.java
    │               │       ├── ReservationRepository.java
    │               │       ├── PaymentRepository.java
    │               │       ├── RefreshTokenRepository.java
    │               │       ├── PasswordService.java
    │               │       └── TokenService.java
    │               │
    │               └── infrastructure
    │                   ├── config
    │                   │   ├── DataInitializer.java
    │                   │   └── OpenApiConfig.java
    │                   │
    │                   ├── exception
    │                   │   ├── ErrorResponse.java
    │                   │   └── GlobalExceptionHandler.java
    │                   │
    │                   ├── persistence
    │                   │   ├── adapters
    │                   │   │   ├── in
    │                   │   │   │   └── rest
    │                   │   │   │       ├── AuthController.java
    │                   │   │   │       ├── UserController.java
    │                   │   │   │       ├── ToolController.java
    │                   │   │   │       ├── ReservationController.java
    │                   │   │   │       └── PaymentController.java
    │                   │   │   │
    │                   │   │   └── out
    │                   │   │       └── persistence
    │                   │   │           ├── UserRepositoryAdapter.java
    │                   │   │           ├── RoleRepositoryAdapter.java
    │                   │   │           ├── ToolRepositoryAdapter.java
    │                   │   │           ├── ReservationRepositoryAdapter.java
    │                   │   │           ├── PaymentRepositoryAdapter.java
    │                   │   │           └── RefreshTokenRepositoryAdapter.java
    │                   │   │
    │                   │   ├── converter
    │                   │   │   └── UuidConverter.java
    │                   │   │
    │                   │   ├── entity
    │                   │   │   ├── UserEntity.java
    │                   │   │   ├── RoleEntity.java
    │                   │   │   ├── ToolEntity.java
    │                   │   │   ├── ReservationEntity.java
    │                   │   │   ├── PaymentEntity.java
    │                   │   │   └── RefreshTokenEntity.java
    │                   │   │
    │                   │   └── repository
    │                   │       ├── UserJpaRepository.java
    │                   │       ├── RoleJpaRepository.java
    │                   │       ├── ToolJpaRepository.java
    │                   │       ├── ReservationJpaRepository.java
    │                   │       ├── PaymentJpaRepository.java
    │                   │       └── RefreshTokenJpaRepository.java
    │                   │
    │                   └── security
    │                       ├── SecurityConfig.java
    │                       ├── JwtAuthenticationFilter.java
    │                       ├── JwtTokenService.java
    │                       └── PasswordServiceImpl.java
    │
    └── resources
        ├── application.yml
        ├── application-dev.yml
        ├── application-prod.yml
        ├── application-local.yml.example
        └── data.sql

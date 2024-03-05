export interface AuthenticationResponse {
    token?: string;
    mfaEnabled?: string;
    secretImageUri?: string;
  }
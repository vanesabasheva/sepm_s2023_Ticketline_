export class RegisterRequest {
  constructor(
    public email: string,
    public firstName: string,
    public lastName: string,
    public password: string,
    public isAdmin: boolean
  ) {
  }
}

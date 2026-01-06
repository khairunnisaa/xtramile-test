export interface Patient {
  patientId?: string;
  id?: string;
  pid?: string;
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
  dob?: string;
  gender?: string;
  phone?: string;
  email?: string;
  identifiers?: Identifier[];
  address?: Address;
  createdAt?: string;
}

export interface Identifier {
  type: string;
  value: string;
}

export interface Address {
  address?: string;
  suburb?: string;
  state?: string;
  postcode?: string;
}


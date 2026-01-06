export interface Patient {
  id?: string;
  firstName: string;
  lastName: string;
  dob: string;
  gender?: string;
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


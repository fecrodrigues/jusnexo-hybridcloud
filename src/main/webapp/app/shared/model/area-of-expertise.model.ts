import { IClient } from 'app/shared/model/client.model';

export interface IAreaOfExpertise {
  id?: number;
  areaName?: string | null;
  isSelected?: boolean | null;
  clients?: IClient[] | null;
}

export const defaultValue: Readonly<IAreaOfExpertise> = {
  isSelected: false,
};

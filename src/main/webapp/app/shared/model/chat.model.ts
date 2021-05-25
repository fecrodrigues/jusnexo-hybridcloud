import dayjs from 'dayjs';
import { IMessage } from 'app/shared/model/message.model';
import { IClient } from 'app/shared/model/client.model';

export interface IChat {
  id?: number;
  createdAt?: string | null;
  messages?: IMessage[] | null;
  clientReceivers?: IClient[] | null;
  clientSenders?: IClient[] | null;
}

export const defaultValue: Readonly<IChat> = {};

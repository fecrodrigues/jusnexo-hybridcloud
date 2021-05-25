import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IClientRating, defaultValue } from 'app/shared/model/client-rating.model';

export const ACTION_TYPES = {
  FETCH_CLIENTRATING_LIST: 'clientRating/FETCH_CLIENTRATING_LIST',
  FETCH_CLIENTRATING: 'clientRating/FETCH_CLIENTRATING',
  CREATE_CLIENTRATING: 'clientRating/CREATE_CLIENTRATING',
  UPDATE_CLIENTRATING: 'clientRating/UPDATE_CLIENTRATING',
  PARTIAL_UPDATE_CLIENTRATING: 'clientRating/PARTIAL_UPDATE_CLIENTRATING',
  DELETE_CLIENTRATING: 'clientRating/DELETE_CLIENTRATING',
  RESET: 'clientRating/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IClientRating>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ClientRatingState = Readonly<typeof initialState>;

// Reducer

export default (state: ClientRatingState = initialState, action): ClientRatingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CLIENTRATING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CLIENTRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CLIENTRATING):
    case REQUEST(ACTION_TYPES.UPDATE_CLIENTRATING):
    case REQUEST(ACTION_TYPES.DELETE_CLIENTRATING):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CLIENTRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CLIENTRATING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CLIENTRATING):
    case FAILURE(ACTION_TYPES.CREATE_CLIENTRATING):
    case FAILURE(ACTION_TYPES.UPDATE_CLIENTRATING):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CLIENTRATING):
    case FAILURE(ACTION_TYPES.DELETE_CLIENTRATING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLIENTRATING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLIENTRATING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CLIENTRATING):
    case SUCCESS(ACTION_TYPES.UPDATE_CLIENTRATING):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CLIENTRATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CLIENTRATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/client-ratings';

// Actions

export const getEntities: ICrudGetAllAction<IClientRating> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CLIENTRATING_LIST,
    payload: axios.get<IClientRating>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IClientRating> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CLIENTRATING,
    payload: axios.get<IClientRating>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IClientRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CLIENTRATING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IClientRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CLIENTRATING,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IClientRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CLIENTRATING,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IClientRating> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CLIENTRATING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

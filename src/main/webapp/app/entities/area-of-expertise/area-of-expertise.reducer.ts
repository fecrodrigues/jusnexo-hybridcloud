import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAreaOfExpertise, defaultValue } from 'app/shared/model/area-of-expertise.model';

export const ACTION_TYPES = {
  FETCH_AREAOFEXPERTISE_LIST: 'areaOfExpertise/FETCH_AREAOFEXPERTISE_LIST',
  FETCH_AREAOFEXPERTISE: 'areaOfExpertise/FETCH_AREAOFEXPERTISE',
  CREATE_AREAOFEXPERTISE: 'areaOfExpertise/CREATE_AREAOFEXPERTISE',
  UPDATE_AREAOFEXPERTISE: 'areaOfExpertise/UPDATE_AREAOFEXPERTISE',
  PARTIAL_UPDATE_AREAOFEXPERTISE: 'areaOfExpertise/PARTIAL_UPDATE_AREAOFEXPERTISE',
  DELETE_AREAOFEXPERTISE: 'areaOfExpertise/DELETE_AREAOFEXPERTISE',
  RESET: 'areaOfExpertise/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAreaOfExpertise>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AreaOfExpertiseState = Readonly<typeof initialState>;

// Reducer

export default (state: AreaOfExpertiseState = initialState, action): AreaOfExpertiseState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AREAOFEXPERTISE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AREAOFEXPERTISE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_AREAOFEXPERTISE):
    case REQUEST(ACTION_TYPES.UPDATE_AREAOFEXPERTISE):
    case REQUEST(ACTION_TYPES.DELETE_AREAOFEXPERTISE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_AREAOFEXPERTISE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_AREAOFEXPERTISE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AREAOFEXPERTISE):
    case FAILURE(ACTION_TYPES.CREATE_AREAOFEXPERTISE):
    case FAILURE(ACTION_TYPES.UPDATE_AREAOFEXPERTISE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_AREAOFEXPERTISE):
    case FAILURE(ACTION_TYPES.DELETE_AREAOFEXPERTISE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_AREAOFEXPERTISE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_AREAOFEXPERTISE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_AREAOFEXPERTISE):
    case SUCCESS(ACTION_TYPES.UPDATE_AREAOFEXPERTISE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_AREAOFEXPERTISE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_AREAOFEXPERTISE):
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

const apiUrl = 'api/area-of-expertises';

// Actions

export const getEntities: ICrudGetAllAction<IAreaOfExpertise> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_AREAOFEXPERTISE_LIST,
    payload: axios.get<IAreaOfExpertise>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAreaOfExpertise> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AREAOFEXPERTISE,
    payload: axios.get<IAreaOfExpertise>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAreaOfExpertise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AREAOFEXPERTISE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAreaOfExpertise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AREAOFEXPERTISE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAreaOfExpertise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_AREAOFEXPERTISE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAreaOfExpertise> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AREAOFEXPERTISE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

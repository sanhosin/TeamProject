package main.java.facade;

import main.java.mgr.Manageable;
import java.util.List;

//아직은 사용하지 않음 추후에 사용예정
public interface IDataEngine<T extends Manageable> {
	// 키워드에 매치되는 것을 모두 찾아 리스트로 반환
	List<T> search(String kwd);
	// UI 테이블의 행에 있는 데이터를 오브젝트 배열로 받아와서 새로운 객체 추가
	void addNewItem(Object[] uiData);
	// UI 테이블의 행에 있는 데이터를 오브젝트 배열로 받아와서 해당 객체 수정
	void update(Object[] uiData);
	// UI 테이블의 행의 첫번째 데이터를 키로 받아와 해당 객체를 찾아 삭제
	void remove(String kwd);
}



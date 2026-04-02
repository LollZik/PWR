    public class OneWaySplittingList<E> extends OneWayLinkedListWithHead<E>{
        private OneWayLinkedListWithHead<OneWayLinkedListWithHead<E>> subLists;

        public OneWaySplittingList(){
            this.subLists = new OneWayLinkedListWithHead<>();
        }

        @Override
        public E get(int index){
            if (index < 0) throw new IndexOutOfBoundsException();

            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                if (index < list.size()) {
                    if (index != 0){
                        Element prevElement = list.getElement(index - 1);
                        E actElem = prevElement.getNext().getValue();
                        split(list, prevElement);
                        return actElem;
                    }
                    return list.getElement(index).getValue();
                }
                index -= list.size();
            }
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        @Override
        public boolean add(E e){
            if(subLists.isEmpty()){
                subLists.add(new OneWayLinkedListWithHead<>());
            }
            return subLists.get(subLists.size()-1).add(e);
        }

        @Override
        public void add(int index, E data) {
            if (index < 0) throw new IndexOutOfBoundsException();
            if(index == 0 && subLists.isEmpty()){
                subLists.add(new OneWayLinkedListWithHead<>());
            }

            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                if (index <= list.size()) {
                    Element newElem = new Element(data);
                    if (index != 0){
                        Element prevElem = list.getElement(index-1);
                        prevElem.setNext(newElem);
                        return;
                    }
                    newElem.setNext(list.head);
                    list.head = newElem;
                    return;
                }
                index -= list.size();
            }
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        public boolean add(OneWayLinkedListWithHead<E> list){
            if (list.equals(null)){
                subLists.add(0,null);
                return true;
            }
            return subLists.add(list);
        }

        @Override
        public E remove(int index){
            if (index < 0) throw new IndexOutOfBoundsException();

            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                if (index < list.size()) {
                    if (index != 0){
                        Element actElem = getElement(index-1);
                        if(actElem.getNext()==null){
                            throw new IndexOutOfBoundsException();
                        }
                        E retValue = actElem.getNext().getValue();
                        actElem.setNext(actElem.getNext().getNext());
                        return retValue;
                    }
                    E retValue= list.head.getValue();
                    list.head=list.head.getNext();
                    if(list.isEmpty()){subLists.remove(i);}
                    return retValue;
                }
                index -= list.size();
            }
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        @Override
        public boolean remove(E value) { // usuwa element (equals())
            if(subLists.isEmpty()){
                return false;
            }
            for(int i = 0;i < subLists.size() ;i++){
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                if(list.remove(value)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int size() {
            int totalSize = 0;
            for (int i = 0; i < subLists.size(); i++) {
                totalSize += subLists.get(i).size();
            }
            return totalSize;
        }

        @Override
        public boolean isEmpty() {
            return subLists.isEmpty();
        }

        @Override
        public void printStructure() {
            System.out.println("Splitting List Structure:");
            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                list.printStructure();
                System.out.println("|\nv");
            }
            System.out.println("☒");
        }

        @Override
        public E set(int index, E data){
            if (index < 0) throw new IndexOutOfBoundsException();

            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                if (index < list.size()) {
                    Element prevElem = list.getElement(index-1);
                    Element actElem = prevElem.getNext();
                    if(index>0){
                        split(list,prevElem);
                    }
                    E elemData = actElem.getValue();
                    actElem.setValue(data);

                    return elemData;
                }
                index -= list.size();
            } throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean contains(E data){
            return indexOf(data)>=0;
        }

        @Override
        public int indexOf(E data){
            int pos = 0;

            for (int i = 0; i < subLists.size(); i++) {
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                Element actElem = list.head;
                while (actElem != null) {
                    if(actElem.getValue().equals(data)) return pos; //Objects.equals(o1, o2);
                    pos++;
                    actElem = actElem.getNext();
                }
            }
            return -1;
        }

        @Override
        public void clear() {
            subLists.head = null;
        }

        public void split(OneWayLinkedListWithHead<E> list, Element prevElement){
            OneWayLinkedListWithHead<E> newList = new OneWayLinkedListWithHead<>();
            newList.head = prevElement.getNext();
            int index = subLists.indexOf(list);
            subLists.add(index +1,newList);
            prevElement.setNext(null);
        }

        @Override
        public void reverse(){
            subLists.reverse();
            for(int i = 0 ; i <subLists.size();i++){
                OneWayLinkedListWithHead<E> list = subLists.get(i);
                list.reverse();
            }
        }
    }
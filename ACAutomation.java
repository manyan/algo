package epi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ACAutomation {
	private class Node{
		int count;
		String word;
		Map<Character, Node> next;
		Node failedNode;
		
		public Node() {
			next = new HashMap<Character, Node>();
		}
	}
	
	private Node root;
	public ACAutomation(Set<String> words) {
		init(words);
	}
	
	private void init(Set<String> words) {
		root = new Node();
		for (String S : words) {
			insert(S);
		}
		
		buildFailedNode();
	}
	
	private void insert(String S) {
		if (S == null || S.length() == 0) {
			return;
		}
		
		Node cur = root;
		int L = S.length();
		for (int i = 0; i < L; i++) {
			char c = S.charAt(i);
			if (!cur.next.containsKey(c)) {
				cur.next.put(c, new Node());
			}
			
			cur = cur.next.get(c);
		}
		
		cur.count++;
		cur.word = S;
	}
	
	private void buildFailedNode() {
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		
		while (!queue.isEmpty()) {
			Node node = queue.poll();
			for (Character c : node.next.keySet()) {
				Node child = node.next.get(c);
				
				Node failedNode = node.failedNode; // init failednode == parent's failed node
				while (failedNode != null) {
					if (failedNode.next.containsKey(c)) {
						failedNode = failedNode.next.get(c);
						break;
					}
					
					failedNode = failedNode.failedNode;
				}
				
				child.failedNode = failedNode == null ? root : failedNode;	
				
				queue.add(child);
			}
		}
	}
	
	public Set<String> search(String S) {
		Set<String> result = new HashSet<String>();
		if (S == null || S.length() == 0) {
			return result;
		}
		
		int L = S.length();
		Node cur = root;
		Set<Node> visited = new HashSet<Node>();
		for (int i = 0; i < L; i++) {
			char c = S.charAt(i);
			while (!cur.next.containsKey(c) && cur != root) {
				cur = cur.failedNode;
			}
			
			cur = cur.next.get(c);
			if (cur == null) {
				cur = root;
			}
			
			Node tmp = cur;
			while (tmp != root && !visited.contains(tmp)) {
				visited.contains(tmp);
				if (tmp.count > 0) {
					result.add(tmp.word);
				}
				
				tmp = tmp.failedNode;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String[] words = {"say","she","shr","he","her"};
		Set<String> ws = new HashSet<String>();
		for (String S : words) {
			ws.add(S);
		}
		ACAutomation aca = new ACAutomation(ws);
		
		String S = "yasherhs";
		Set<String> set = aca.search(S);
		for (String s : set) {
			System.out.println(s);
		}
	}
}
